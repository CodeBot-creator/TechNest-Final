package com.example.technest_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    BottomNavigationView bottomNavigationView;
    List<NewsItem> newsList = new ArrayList<>();
    String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loggedInUsername = getIntent().getStringExtra("username");

        ImageView infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));

        ImageView profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", loggedInUsername);
            startActivityForResult(intent, 1001);
        });

        FloatingActionButton fab = findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra("category", "home");
            startActivity(intent);
        });

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsAdapter(this, newsList));

        loadNewsFromFirebase("home");

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.nav_academic) {
                intent = new Intent(this, AcademicActivity.class);
            } else if (item.getItemId() == R.id.nav_event) {
                intent = new Intent(this, EventsActivity.class);
            } else return true;

            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
            finish();
            return true;
        });
    }

    private void loadNewsFromFirebase(String category) {
        FirebaseDatabase.getInstance("https://technest-final-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
                .child(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        newsList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String title = data.child("title").getValue(String.class);
                            String desc = data.child("description").getValue(String.class);
                            String date = data.child("date").getValue(String.class);
                            newsList.add(new NewsItem(title, desc, date));
                        }
                        newsRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            loggedInUsername = data.getStringExtra("updatedUsername");
        }
    }
}
