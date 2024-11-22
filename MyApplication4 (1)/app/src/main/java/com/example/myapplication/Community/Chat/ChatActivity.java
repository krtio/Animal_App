package com.example.myapplication.Community.Chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatCallback {

    private WebSocketManager webSocketManager;
    private EditText inputMessage;
    private TextView chatTitle, chatLog;
    private ImageButton backButton;
    private Button deleteButton;
    private String category, title, userName, sessionId;
    private List<String> chatMessages = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_meeting);

        inputMessage = findViewById(R.id.inputMessage);
        chatTitle = findViewById(R.id.chTitle);
        Button sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.back_button);
        chatLog = findViewById(R.id.chatLog);

        category = getIntent().getStringExtra("category");
        title = getIntent().getStringExtra("title");
        userName = getIntent().getStringExtra("userName");
        sessionId = getIntent().getStringExtra("sessionId");
        chatTitle.setText(title);

        backButton.setOnClickListener(v -> onBackPressed());

        ChatWebSocketListener listener = new ChatWebSocketListener(this);
        webSocketManager = new WebSocketManager(listener);
        webSocketManager.connect();

        sendButton.setOnClickListener(v -> {
            String message = inputMessage.getText().toString();
            if (!message.isEmpty()) {
                webSocketManager.sendMessage(message);
                inputMessage.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "메시지를 입력하세요!", Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(v -> {
            webSocketManager.disconnect(); // 서버와의 연결 종료
            chatMessages.clear(); // 채팅 메시지 초기화
            updateChatLog(); // 화면에 갱신된 내용 반영
            Toast.makeText(ChatActivity.this, "채팅을 종료하고 나갔습니다.", Toast.LENGTH_SHORT).show();
            finish(); // 액티비티 종료
        });
    }

    @Override
    public void onConnected() {
        runOnUiThread(() -> {
            chatMessages.add("서버에 연결되었습니다");
            updateChatLog();
        });
    }

    @Override
    public void onMessageReceived(String message) {
        runOnUiThread(() -> {
            chatMessages.add(message);
            updateChatLog();
        });
    }

    @Override
    public void onError(Throwable error) {
        runOnUiThread(() -> {
            chatMessages.add("오류: " + error.getMessage());
            updateChatLog();
        });
    }

    @Override
    public void onDisconnected(String reason) {
        runOnUiThread(() -> {
            chatMessages.add("서버와 연결이 끊어졌습니다: " + reason);
            updateChatLog();
        });
    }

    private void updateChatLog() {
        StringBuilder chatHistory = new StringBuilder();
        for (String message : chatMessages) {
            chatHistory.append(message).append("\n");
        }
        chatLog.setText(chatHistory.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketManager.disconnect();
    }
}
