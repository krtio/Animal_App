package com.example.demo.Comment;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public Comment() {}

    public Comment(String userId, String content, Long postId) {
        this.userId = userId;
        this.content = content;
        this.postId = postId;
    }

    // Getter and Setter
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public Long getPostId() { return postId; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setContent(String content) { this.content = content; }
    public void setPostId(Long postId) { this.postId = postId; }
}

