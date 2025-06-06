package com.example.technest_final;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    EditText usernameEditText, emailEditText;
    AppCompatButton saveButton, logoutButton, deleteButton;
    ImageView backButton;

    String loggedInUsername;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        loggedInUsername = getIntent().getStringExtra("username");

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Toast.makeText(this, "Username not received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reference = FirebaseDatabase.getInstance("https://technest-final-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users").child(loggedInUsername);

        loadUserProfile();

        saveButton.setOnClickListener(v -> updateUserProfile());
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        backButton.setOnClickListener(v -> finish());
    }

    private void loadUserProfile() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    usernameEditText.setText(uname != null ? uname : "");
                    emailEditText.setText(email != null ? email : "");
                } else {
                    Toast.makeText(ProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String updatedUsername = usernameEditText.getText().toString().trim();
        String updatedEmail = emailEditText.getText().toString().trim();

        if (updatedUsername.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!updatedUsername.equals(loggedInUsername)) {
            // Step 1: Read old user data (password)
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String password = snapshot.child("password").getValue(String.class);

                        // Step 2: Create new user node with updated values
                        DatabaseReference newRef = reference.getParent().child(updatedUsername);
                        newRef.setValue(new User(updatedUsername, updatedEmail, password))
                                .addOnSuccessListener(aVoid -> {
                                    // Step 3: Remove old node
                                    reference.removeValue().addOnSuccessListener(unused -> {
                                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("updatedUsername", updatedUsername);
                                        setResult(RESULT_OK, resultIntent);
                                        loggedInUsername = updatedUsername;
                                        reference = newRef; // update reference
                                        showSuccessDialog();
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProfileActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(ProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Just update email
            reference.child("email").setValue(updatedEmail)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedUsername", updatedUsername);
                        setResult(RESULT_OK, resultIntent);
                        showSuccessDialog();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }



    private void showLogoutConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_logout_confirm);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button yesBtn = dialog.findViewById(R.id.yesButton);
        Button noBtn = dialog.findViewById(R.id.noButton);

        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            setResult(RESULT_CANCELED);
            Toast.makeText(ProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        noBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDeleteConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_confirm);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button yesBtn = dialog.findViewById(R.id.yesButton);
        Button noBtn = dialog.findViewById(R.id.noButton);

        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            deleteUserFromFirebase();
        });

        noBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void deleteUserFromFirebase() {
        reference.removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    showDeleteDialog();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete account: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showDeleteDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_deleted);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        runnable = () -> {
            dialog.dismiss();
            Intent intent = new Intent(ProfileActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        };
        handler.postDelayed(runnable, 3000);
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        runnable = () -> {
            dialog.dismiss();
            finish(); // Now close after success dialog
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
