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

public class EventsActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    BottomNavigationView bottomNavigationView;
    List<NewsItem> newsList = new ArrayList<>();

    // ✅ Declare here — class-level variable
    String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // same layout reused

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // ✅ Get username once and assign to class-level variable
        loggedInUsername = getIntent().getStringExtra("username");

        ImageView infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EventsActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        ImageView profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EventsActivity.this, ProfileActivity.class);
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
        });

        // Event dummy news
        newsList.add(new NewsItem("Annual Tech Fest is Coming! 🚀",
                "Join us for TechNova 2025 from April 15–17. Explore innovation, coding, and more.",
                "2025/03/30", R.drawable.news2));

        newsList.add(new NewsItem("Cultural Night 🎉",
                "Experience a night of music, dance, and tradition. April 5 at the Main Hall.",
                "2025/03/31", R.drawable.news2));

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsAdapter(newsList));

        bottomNavigationView.setSelectedItemId(R.id.nav_event);
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

            // ✅ Now this works perfectly
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
            finish();
            return true;
        });
    }
}
