package com.example.technest_final;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;
        ImageView image;

        public NewsViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.newsTitle);
            description = view.findViewById(R.id.newsDescription);
            date = view.findViewById(R.id.newsDate);
            image = view.findViewById(R.id.newsImage);
        }
    }

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
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
        holder.date.setText(item.date);
        holder.image.setImageResource(item.imageResId);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
