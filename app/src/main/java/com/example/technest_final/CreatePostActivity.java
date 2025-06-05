package com.example.technest_final;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    EditText titleEditText, descriptionEditText;
    Button uploadBtn;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        titleEditText = findViewById(R.id.editTitle);
        descriptionEditText = findViewById(R.id.editDescription);
        uploadBtn = findViewById(R.id.uploadButton);

        category = getIntent().getStringExtra("category");
        if (category == null) {
            Toast.makeText(this, "Error: No category specified.", Toast.LENGTH_SHORT).show();
            finish();
        }

        uploadBtn.setOnClickListener(v -> uploadPost());

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void uploadPost() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Both title and description are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        String postId = UUID.randomUUID().toString();
        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("id", postId);
        postMap.put("title", title);
        postMap.put("description", description);
        postMap.put("date", date);

        Log.d("FIREBASE_UPLOAD", "Uploading to category: " + category);
        Log.d("FIREBASE_UPLOAD", "Post Data: " + postMap);

        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://technest-final-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
                .child(category);
        dbRef.child(postId).setValue(postMap)
                .addOnSuccessListener(unused -> {
                    Log.d("FIREBASE_UPLOAD", "✅ Upload successful to category: " + category);
                    Toast.makeText(this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                    showSuccessDialog();

                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE_UPLOAD", "❌ Upload failed", e);
                    Toast.makeText(this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            finish(); // ✅ Close activity AFTER dialog is shown fully
        };
        handler.postDelayed(runnable, 3000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}
