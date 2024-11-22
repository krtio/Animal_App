package com.example.myapplication.Community;

import java.io.Serializable;

public class Post implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String createdAt;
    private String userId;
    private String imageData;

    public Post(String title, String content, String category, String userId, String imageData) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.userId = userId;
        this.imageData = imageData;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getCreatedAt() { return createdAt; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }
}
