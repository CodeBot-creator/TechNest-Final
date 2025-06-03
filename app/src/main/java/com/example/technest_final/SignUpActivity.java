package com.example.technest_final;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    TextInputEditText usernameInput, emailInput, passwordInput, confirmPasswordInput;
    Button signupBtn;
    ImageButton backButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signupBtn = findViewById(R.id.signupBtn);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        signupBtn.setOnClickListener(v -> {

            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String pass = passwordInput.getText().toString();
            String confirm = confirmPasswordInput.getText().toString();

            if (username.isEmpty()) {
                usernameInput.setError("Username is required");
                usernameInput.requestFocus();
            } else if (email.isEmpty()) {
                emailInput.setError("Email is required");
                emailInput.requestFocus();
            } else if (pass.isEmpty()) {
                passwordInput.setError("Password is required");
                passwordInput.requestFocus();
            } else if (confirm.isEmpty()) {
                confirmPasswordInput.setError("Please confirm password");
                confirmPasswordInput.requestFocus();
            } else if (!pass.equals(confirm)) {
                confirmPasswordInput.setError("Passwords do not match");
                confirmPasswordInput.requestFocus();
            } else {
                database = FirebaseDatabase.getInstance("https://technest-final-default-rtdb.asia-southeast1.firebasedatabase.app/");
                reference = database.getReference("users");

                HelperClass helperClass = new HelperClass(username, email, pass);
                reference.child(username).setValue(helperClass)
                        .addOnSuccessListener(aVoid -> showSuccessDialog())
                        .addOnFailureListener(e -> Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        runnable = () -> {
            dialog.dismiss();
            startActivity(new Intent(SignUpActivity.this, Login.class));
            finish();
        };

        handler.postDelayed(runnable, 2500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
