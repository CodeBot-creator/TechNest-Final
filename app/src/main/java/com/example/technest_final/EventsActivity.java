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

public class EventsActivity extends AppCompatActivity {

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
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra("category", "event");
            startActivity(intent);
        });

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsAdapter(this, newsList));

        loadNewsFromFirebase("event");

        bottomNavigationView.setSelectedItemId(R.id.nav_event);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_academic) {
                intent = new Intent(this, AcademicActivity.class);
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
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
}
