package com.lantien.bediss.wave;

import android.graphics.Bitmap;

public class Post {

    private String title;
    private Bitmap imageUrl;

    public Post() {
        title = "";
        imageUrl = null;
    }

    public Post(String title, Bitmap imageUrl){

        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImageUrl() {
        return imageUrl;
    }
}
