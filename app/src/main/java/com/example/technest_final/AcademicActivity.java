package com.example.technest_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcademicActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    List<NewsItem> newsList = new ArrayList<>();
    NewsAdapter adapter;
    String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Shared layout

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fabCreatePost);

        loggedInUsername = getIntent().getStringExtra("username");

        ImageView infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));

        ImageView profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
        });

        adapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(adapter);

        loadNewsFromFirebase("academic");

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra("category", "academic");
            startActivity(intent);
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_academic);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(this, MainActivity.class);
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
                        adapter.notifyDataSetChanged(); // 🔄 Refresh UI
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Optional: log or show a Toast
                    }
                });
    }

}
