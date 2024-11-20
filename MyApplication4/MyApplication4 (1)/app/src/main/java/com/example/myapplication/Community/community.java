package com.example.myapplication.Community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Community.Chat.ChatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class community extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Post> postListFull;
    private Button btnAll, btnNotice0, btnNotice1, btnNotice2, btnNotice3, btnNotice4;
    private FloatingActionButton fab;
    private SearchView searchView;
    private String userName;

    public community() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new ArrayList<>();
        postListFull = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank3, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAll = view.findViewById(R.id.btnAll);
        btnNotice0 = view.findViewById(R.id.btnNotice0);
        btnNotice1 = view.findViewById(R.id.btnNotice1);
        btnNotice2 = view.findViewById(R.id.btnNotice2);
        btnNotice3 = view.findViewById(R.id.btnNotice3);
        btnNotice4 = view.findViewById(R.id.btnNotice4);
        fab = view.findViewById(R.id.fab);
        searchView = view.findViewById(R.id.search_view);

        userName = ((MainActivity) getActivity()).getLoggedInUserName();

        setupRecyclerView();
        setupButtons();
        setupSearchView();

        fetchPosts();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener(post -> {
            String category = post.getCategory();

            if ("모임게시판".equals(category)) {
                openChatSession(post);
            } else {
                Intent intent = new Intent(getActivity(), PreviewPostActivity.class);
                intent.putExtra("title", post.getTitle());
                intent.putExtra("content", post.getContent());
                intent.putExtra("category", post.getCategory());
                intent.putExtra("created_at", post.getCreatedAt());
                startActivity(intent);
            }
        });
    }

    private void openChatSession(Post post) {
        String category = post.getCategory();
        String title = post.getTitle();
        String userName = ((MainActivity) getActivity()).getLoggedInUserName();
        String sessionId = "session-id";

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("title", title);
        intent.putExtra("userName", userName);
        intent.putExtra("sessionId", sessionId);
        startActivity(intent);
    }



    private void fetchImagePath(Long postId) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<String> call = apiService.getImagePath(postId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imagePath = response.body();
                    Log.d("ImagePath", "이미지 경로: " + imagePath);

                    String baseUrl = "http://192.168.2.12:80"; // 서버 URL
                    String fullImagePath = baseUrl + imagePath; // 전체 URL 구성
                    showToast("이미지 경로: " + fullImagePath);
                } else {
                    showToast("이미지 경로를 가져오는 데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("오류: " + t.getMessage());
            }
        });
    }

    private void setupButtons() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreatePostActivity.class);
            intent.putExtra("user_name", userName);
            startActivity(intent);
        });

        btnAll.setOnClickListener(v -> fetchPosts());
        btnNotice0.setOnClickListener(v -> fetchPostsByCategory(btnNotice0.getText().toString()));
        btnNotice1.setOnClickListener(v -> fetchPostsByCategory(btnNotice1.getText().toString()));
        btnNotice2.setOnClickListener(v -> fetchPostsByCategory(btnNotice2.getText().toString()));
        btnNotice3.setOnClickListener(v -> fetchPostsByCategory(btnNotice3.getText().toString()));
        btnNotice4.setOnClickListener(v -> fetchPostsByCategory(btnNotice4.getText().toString()));
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPosts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchPosts() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Post>> call = apiService.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postListFull.clear();
                    postList.addAll(response.body());
                    postListFull.addAll(response.body());
                    sortPostsByTime();
                    postAdapter.notifyDataSetChanged();
                } else {
                    showToast("게시글을 가져오는 데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showToast("오류: " + t.getMessage());
            }
        });
    }

    private void fetchPostsByCategory(String category) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Post>> call = apiService.getPostsByCategory(category);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    sortPostsByTime();
                    postAdapter.notifyDataSetChanged();
                } else {
                    showToast("게시글을 가져오는 데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showToast("오류: " + t.getMessage());
            }
        });
    }

    private void sortPostsByTime() {
        Collections.sort(postList, (p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
    }

    private void filterPosts(String query) {
        List<Post> filteredList = new ArrayList<>();
        for (Post post : postListFull) {
            if (post.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    post.getContent().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(post);
            }
        }

        if (!filteredList.isEmpty()) {
            Intent intent = new Intent(getActivity(), SearchResultFragment.class);
            intent.putExtra("searchResults", (Serializable) filteredList);
            startActivity(intent);
        } else {
            showToast("검색 결과가 없습니다.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
