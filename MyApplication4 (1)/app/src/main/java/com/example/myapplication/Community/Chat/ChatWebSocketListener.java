package com.example.myapplication.Community.Chat;

import android.util.Log;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatWebSocketListener extends WebSocketListener {
    private static final String TAG = "WebSocketListener";
    private final ChatCallback callback;

    public ChatWebSocketListener(ChatCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "WebSocket Opened");
        callback.onConnected();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(TAG, "Received: " + text);
        callback.onMessageReceived(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "WebSocket Closing: " + reason);
        callback.onDisconnected(reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAG, "WebSocket Error: " + t.getMessage());
        callback.onError(t);
    }
}
