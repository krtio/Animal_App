package com.example.myapplication.Community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Spinner spCategory;
    private Button btnSave;
    private ImageButton backButton, imageButton;
    private ImageView selectedImageView;  // 이미지가 표시될 ImageView
    private String userName;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_create);

        userName = getIntent().getStringExtra("user_name");
        if (userName == null) {
            Toast.makeText(this, "사용자 이름이 전달되지 않았습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        backButton = findViewById(R.id.back_button);
        imageButton = findViewById(R.id.image_button);
        selectedImageView = findViewById(R.id.selectedImageView);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        spCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSave);

        backButton.setOnClickListener(v -> onBackPressed());
        imageButton.setOnClickListener(v -> selectImageFromGallery());

        setupCategorySpinner();
        btnSave.setOnClickListener(v -> savePost());
    }

    private void setupCategorySpinner() {
        String[] categories = {"자유게시판", "실종동물게시판", "나눔게시판", "공동구매게시판", "모임게시판"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                if ("실종동물게시판".equals(selectedCategory)) {
                    etContent.setText("1. 실종 위치 :\n\n2. 실종 날짜 :\n\n3. 연락 방법 : \n\n4. 품종 : \n\n5. 나이 : ");
                }
                else if ("나눔게시판".equals(selectedCategory)) {
                    etContent.setText("1. 제품 명 :\n\n2. 위치 :\n\n3. 연락 방법 : \n\n4. 상태 : ");
                }
                else if ("모임게시판".equals(selectedCategory)) {
                    etContent.setVisibility(View.GONE);
                    etContent.setText(userName);
                } else {
                    etContent.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etContent.setHint("내용을 입력하세요");
            }
        });
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // 선택한 이미지를 ImageView로 표시
            selectedImageView.setVisibility(View.VISIBLE);  // 이미지가 선택되면 ImageView를 보이게 함
            selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private void savePost() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrofit 요청 준비
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        RequestBody requestBodyTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody requestBodyContent = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody requestBodyCategory = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("text/plain"), userName);

        MultipartBody.Part filePart = null;
        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] imageBytes = stream.toByteArray();

                RequestBody requestBodyImage = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                filePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBodyImage);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Call<Post> call = apiService.createPostWithImage(requestBodyTitle, requestBodyContent, requestBodyCategory, requestBodyUserId, filePart);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreatePostActivity.this, "게시글이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreatePostActivity.this, "게시글 저장 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CreatePostActivity.this, "서버와의 통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
