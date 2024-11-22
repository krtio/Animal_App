package com.example.myapplication.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends AppCompatActivity {

    private EditText etId, etPassword, etName, etEmail, etPhone, etBirthday;
    private Button continueButton, registerButton, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etBirthday = findViewById(R.id.etBirthday);
        continueButton = findViewById(R.id.btnContinue);
        registerButton = findViewById(R.id.register_button);
        btnLogin = findViewById(R.id.btnLogin);

        resetUI();

        continueButton.setOnClickListener(v -> {
            String inputId = etId.getText().toString().trim();
            checkUserId(inputId);
        });

        registerButton.setOnClickListener(v -> registerUser());

        btnLogin.setOnClickListener(v -> {
            String inputId = etId.getText().toString().trim();
            String inputPassword = etPassword.getText().toString().trim();
            loginUser(inputId, inputPassword);
        });

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String inputId = etId.getText().toString().trim();
                String inputPassword = etPassword.getText().toString().trim();
                loginUser(inputId, inputPassword);
                return true;
            }
            return false;
        });
    }

    private void resetUI() {
        etPassword.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        etEmail.setVisibility(View.GONE);
        etPhone.setVisibility(View.GONE);
        etBirthday.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
    }

    private void registerUser() {
        User newUser = new User(
                etId.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etBirthday.getText().toString().trim()
        );

        UserAPI userAPI = RetrofitClient.getInstance().create(UserAPI.class);
        Call<Void> call = userAPI.registerUser(newUser);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(login_activity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    resetUI(); // UI를 리셋하여 초기 상태로 되돌림
                } else {
                    Toast.makeText(login_activity.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(login_activity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserId(String id) {
        UserAPI userAPI = RetrofitClient.getInstance().create(UserAPI.class);
        Call<User> call = userAPI.getUserById(id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    etPassword.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.GONE);
                    Toast.makeText(login_activity.this, "아이디 확인 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(login_activity.this, "아이디가 존재하지 않습니다. 회원가입을 진행하세요.", Toast.LENGTH_SHORT).show();
                    showRegisterFields();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(login_activity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRegisterFields() {
        etPassword.setVisibility(View.VISIBLE);
        etName.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.VISIBLE);
        etBirthday.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
    }

    private void loginUser(String id, String password) {
        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        User loginUser = new User();
        loginUser.setId(id);
        loginUser.setPassword(password);

        UserAPI userAPI = RetrofitClient.getInstance().create(UserAPI.class);
        Call<User> call = userAPI.loginUser(loginUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    if (loggedInUser != null) {
                        String userName = loggedInUser.getName();
                        handleSuccessfulLogin(userName);
                    } else {
                        Toast.makeText(login_activity.this, "로그인 실패: 사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "로그인 실패";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(login_activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(login_activity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSuccessfulLogin(String userName) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("logged_in_user_name", userName);
        editor.apply();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("user_name", userName);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
