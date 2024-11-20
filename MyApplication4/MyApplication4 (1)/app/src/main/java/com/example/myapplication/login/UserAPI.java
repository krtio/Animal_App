package com.example.myapplication.login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {
    @GET("/api/user/{id}")
    Call<User> getUserById(@Path("id") String id);

    @POST("/api/user/login") // POST 방식으로 변경
    Call<User> loginUser(@Body User loginUser); // User 객체를 요청 본문으로 전달

    @POST("/api/user/save")
    Call<Void> registerUser(@Body User user);

    @GET("/api/users/profile")
    Call<User> getUserProfile(@Query("name") String userId);
}
