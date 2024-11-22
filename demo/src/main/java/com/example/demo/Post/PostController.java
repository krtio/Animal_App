package com.example.demo.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService fileStorageService;

    // 모든 게시물 조회
    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<String> imageData(@PathVariable Long id) {
        Optional<String> imageData = postService.getImagePathByPostId(id);
        if (imageData.isPresent()) {
            return ResponseEntity.ok(imageData.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미지 경로가 없습니다.");
        }
    }

    // 카테고리별 게시물 조회
    @GetMapping("/category")
    public ResponseEntity<List<Post>> getPostsByCategory(@RequestParam String category) {
        List<Post> posts = postService.getPostsByCategory(category);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    // 제목으로 게시물 조회
    @GetMapping("/title")
    public ResponseEntity<List<Post>> getPostByTitle(@RequestParam String title) {
        List<Post> posts = postService.getPostByTitle(title);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    // 게시물 생성 (이미지 포함 가능)
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam("userId") String userId,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String imageData = null;

            // 이미지 파일이 있을 경우 처리
            if (file != null && !file.isEmpty()) {
                imageData = fileStorageService.storeFile(file); // 이미지 저장 후 경로 반환
            }

            // 게시물 객체 생성
            Post post = new Post(title, content, category, userId, imageData);
            post.setCreatedAt(LocalDateTime.now());
            Post savedPost = postService.createPost(post); // 게시물 저장

            // 저장된 게시물 응답 반환 (이미지 경로 포함)
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 실패 시 null 반환
        }

    }
}

