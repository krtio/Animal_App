package com.example.myapplication.Community;

import com.example.myapplication.Community.Comment.Comment;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Part;

public interface ApiService {

    @GET("/api/posts")
    Call<List<Post>> getPosts();

    @GET("/api/posts/category")
    Call<List<Post>> getPostsByCategory(@Query("category") String category);

    @POST("/api/posts")
    Call<Post> createPost(@Body Post post);

    @POST("/api/posts")
    @Multipart
    Call<Post> createPostWithImage(
            @Part("title") RequestBody title,
            @Part("content") RequestBody content,
            @Part("category") RequestBody category,
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image
    );
    @POST("/api/posts/upload")
    @Multipart
    Call<String> uploadImage(@Part MultipartBody.Part file);

    @POST("/api/comments")
    Call<Comment> createComment(@Body Comment comment);

    @GET("/api/posts")
    Call<List<Post>> getPostByTitle(@Query("title") String title);

    @GET("/api/posts")
    Call<List<Post>> getPostByid(@Query("id") String id);

    @GET("/api/posts")
    Call<List<Post>> getPostByImageData(@Query("imageData") String imageData);

    @GET("/api/posts/{id}/image")
    Call<String> getImagePath(@Path("id") Long id);

    @GET("/api/comments/{postId}")
    Call<List<Comment>> getCommentsByPostId(@Path("postId") Long postId);
}
