package com.example.technest_final;

public class NewsItem {
    String title, description, date;
    int imageResId;

    public NewsItem(String title, String description, String date, int imageResId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResId = imageResId;
    }
}
