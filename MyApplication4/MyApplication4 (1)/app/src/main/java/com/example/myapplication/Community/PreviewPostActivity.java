package com.example.myapplication.Community;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Community.Comment.Comment;
import com.example.myapplication.Community.Comment.CommentAdapter;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class PreviewPostActivity extends AppCompatActivity {
    private TextView tvContent, tvCategory, tvCreatedDate, username, tvTitle, btnJoinCount;
    private EditText etMessageInput;
    private ImageButton btnSendMessage, backButton;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private long postId = -1;
    private String currentUserId;
    private ImageView ivPostImage;
    private Button btnJoinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_preview);

        tvTitle = findViewById(R.id.tvTitle);
        backButton = findViewById(R.id.back_button);
        tvContent = findViewById(R.id.tvContent);
        tvCategory = findViewById(R.id.tvCategory);
        tvCreatedDate = findViewById(R.id.tv_post_date);
        etMessageInput = findViewById(R.id.et_message_input);
        btnSendMessage = findViewById(R.id.btn_send_message);
        rvComments = findViewById(R.id.rv_comments);
        username = findViewById(R.id.tv_user_name);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnJoinGroup = findViewById(R.id.btn_join_group);
        btnJoinCount = findViewById(R.id.tv_join_count);

        commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        backButton.setOnClickListener(v -> onBackPressed());

        String title = getIntent().getStringExtra("title");
        if (title == null) {
            showToast("제목을 불러올 수 없습니다.");
            finish();
            return;
        }

        currentUserId = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("logged_in_user_name", null);
        if (currentUserId == null || currentUserId.isEmpty()) {
            showToast("사용자 ID를 불러올 수 없습니다.");
            finish();
            return;
        }

        fetchPostIdByTitle(title);

        btnSendMessage.setOnClickListener(view -> {
            String commentText = etMessageInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                saveComment(commentText);
            } else {
                showToast("댓글을 입력해주세요.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(PreviewPostActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void fetchPostIdByTitle(String title) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Post>> call = apiService.getPostByTitle(title);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    for (Post post : response.body()) {
                        if (post.getTitle().equals(title)) {
                            postId = post.getId();
                            updateUI(post); // UI 업데이트
                            fetchComments(); // 댓글 불러오기
                            return;
                        }
                    }
                    showToast("해당 포스트를 찾을 수 없습니다.");
                } else {
                    showToast("포스트 정보를 불러올 수 없습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showToast("포스트 정보를 불러오는 중 오류 발생");
            }
        });
    }

    private void updateUI(Post post) {
        clearGlideCache();

        tvTitle.setText(post.getTitle());
        tvContent.setText(post.getContent());
        tvCategory.setText(post.getCategory());
        username.setText(post.getUserId());
        tvCreatedDate.setText("작성일자: " + post.getCreatedAt());

        if ("공동구매게시판".equals(post.getCategory())) {
            btnJoinGroup.setVisibility(View.VISIBLE);
            btnJoinCount.setVisibility(View.VISIBLE);
        } else {
            btnJoinGroup.setVisibility(View.GONE);
            btnJoinCount.setVisibility(View.GONE);
        }

        String imagePath = post.getImageData();
        String baseUrl = "http://192.168.2.12:80"; // 서버 URL
        if (imagePath != null && !imagePath.isEmpty()) {
            String imageUrl = baseUrl + imagePath;
            Log.d("Image URL", "이미지 URL: " + imageUrl);

            // 이미지 로딩
            loadImage(imageUrl);
            ivPostImage.setVisibility(View.VISIBLE);  // 이미지가 있으면 보이게
        } else {
            ivPostImage.setVisibility(View.GONE);  // 이미지가 없으면 숨기기
            Log.d("Image URL", "이미지가 없습니다.");
        }
    }

    private void clearGlideCache() {
        Glide.get(this).clearMemory();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache(); // 백그라운드에서 디스크 캐시 비우기
                Log.d("Glide Cache", "디스크 캐시 비우기 완료");
            }
        }).start();
    }

    private void loadImage(String imageUrl) {
        Log.d("Load Image", "로딩 중인 이미지 URL: " + imageUrl);

        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .override(ivPostImage.getWidth(), ivPostImage.getHeight())  // ImageView 크기에 맞추기
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPostImage);

        Log.d("Load Image", "이미지 로딩 요청 완료");
    }

    private void fetchComments() {
        if (postId == -1) {
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Comment>> call = apiService.getCommentsByPostId(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentList.clear();
                    commentList.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    showToast("댓글을 불러오는 중 문제가 발생했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                showToast("댓글을 불러오는 중 오류 발생");
            }
        });
    }

    private void saveComment(String commentText) {
        if (currentUserId == null) {
            showToast("로그인 상태를 확인해주세요.");
            return;
        }
        if (postId == -1) {
            showToast("유효하지 않은 포스트 ID입니다.");
            return;
        }

        Comment newComment = new Comment(currentUserId, commentText, postId);

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<Comment> call = apiService.createComment(newComment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    showToast("댓글이 등록되었습니다.");
                    fetchComments();
                    etMessageInput.setText(""); // 입력 필드 초기화
                } else {
                    showToast("댓글 등록 실패");
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                showToast("댓글 등록 중 오류 발생");
            }
        });
    }
}
