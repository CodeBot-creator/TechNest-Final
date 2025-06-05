package com.example.technest_final;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private Context context;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;

        public NewsViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.newsTitle);
            description = view.findViewById(R.id.newsDescription);
            date = view.findViewById(R.id.newsDate);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.date.setText("Published: " + item.date);

        // Handle card click
        holder.itemView.setOnClickListener(v -> {
            // Example: show a toast (can be replaced with detail screen)
            Toast.makeText(context, "Clicked: " + item.title, Toast.LENGTH_SHORT).show();

            // Optional: Launch detail screen
            /*
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", item.title);
            intent.putExtra("description", item.description);
            intent.putExtra("date", item.date);
            context.startActivity(intent);
            */
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
