package com.example.demo.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitle(String title);
    List<Post> findByCategory(String category);
    List<Post> findByTitleContaining(String title);

    @Query("SELECT p.imageData FROM Post p WHERE p.id = :id")
    Optional<String> findImagePathById(@Param("id") Long id);

}

