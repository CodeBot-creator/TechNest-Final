package com.example.technest_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        ImageView profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("username", loggedInUsername);
            startActivityForResult(intent, 1001); // updated for result
        });

        // Dummy News
        newsList.add(new NewsItem("Get Ready for Interfaculty Championship 2025! 🎯",
                "The stage is set, the rivalries are heating up...",
                "2025/03/21", R.drawable.news2));

        newsList.add(new NewsItem("Cricket League Starts Next Week!",
                "Join us at the stadium for action-packed cricket.",
                "2025/03/25", R.drawable.news2));

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsAdapter(newsList));

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_academic) {
                Intent intent = new Intent(this, AcademicActivity.class);
                intent.putExtra("username", loggedInUsername);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_event) {
                Intent intent = new Intent(this, EventsActivity.class);
                intent.putExtra("username", loggedInUsername);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
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
