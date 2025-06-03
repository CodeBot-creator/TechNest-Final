package com.example.technest_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AcademicActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    BottomNavigationView bottomNavigationView;
    List<NewsItem> newsList = new ArrayList<>();

    // ✅ Moved to class level
    String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // reusing same layout

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // ✅ Assign it here
        loggedInUsername = getIntent().getStringExtra("username");

        ImageView infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AcademicActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        ImageView profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AcademicActivity.this, ProfileActivity.class);
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
        });

        // Academic dummy news
        newsList.add(new NewsItem("Exam Schedule Released 📝",
                "Final semester exam timetable is out now. Check LMS for details.",
                "2025/03/28", R.drawable.news2));

        newsList.add(new NewsItem("Research Paper Submission Deadline ⏳",
                "Don't forget to submit your final year project report by April 10.",
                "2025/03/29", R.drawable.news2));

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsAdapter(newsList));

        bottomNavigationView.setSelectedItemId(R.id.nav_academic);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.nav_home) {
                intent = new Intent(this, MainActivity.class);
            } else if (id == R.id.nav_academic) {
                intent = new Intent(this, AcademicActivity.class);
            } else if (id == R.id.nav_event) {
                intent = new Intent(this, EventsActivity.class);
            } else {
                return false;
            }

            // ✅ Pass username forward
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
            finish();
            return true;
        });
    }
}
