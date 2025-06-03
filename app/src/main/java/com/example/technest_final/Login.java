package com.example.technest_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginBtn;
    TextView signUpLink;  // 👈 New variable for Sign Up redirect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);  // 👈 Connect the Sign Up TextView

        // 👉 Login logic
        loginBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference reference = FirebaseDatabase.getInstance("https://technest-final-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("users");

            reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String passwordFromDB = snapshot.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(password)) {
                            usernameInput.setError(null);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("username", username); // ✅ Pass username to MainActivity
                            Toast.makeText(Login.this, "Logged in as: " + username, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // ✅ Sign Up redirection logic
        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUpActivity.class); // 👈 Change to your actual Register Activity class
            startActivity(intent);
        });
    }
}
