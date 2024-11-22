package com.example.myapplication.Community.Comment;

public class Comment {
    private Long id;
    private String userId;
    private String content;
    private Long postId;

    public Comment(String userId, String content, Long postId) {
        this.userId = userId;
        this.content = content;
        this.postId = postId;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public Long getPostId() { return postId; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setContent(String content) { this.content = content; }
    public void setPostId(Long postId) { this.postId = postId; }
}
