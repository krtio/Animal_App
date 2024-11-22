package com.example.myapplication.Community.Chat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {
    private static final String SOCKET_URL = "ws://192.168.2.12:8080/chat"; // 서버 주소와 엔드포인트 수정
    private WebSocket webSocket;
    private WebSocketListener listener;

    public WebSocketManager(WebSocketListener listener) {
        this.listener = listener;
    }

    public void connect() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SOCKET_URL).build();
        webSocket = client.newWebSocket(request, listener);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Client Closed");
        }
    }
}
