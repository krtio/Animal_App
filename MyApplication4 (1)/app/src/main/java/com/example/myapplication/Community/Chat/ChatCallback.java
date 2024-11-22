package com.example.myapplication.Community.Chat;

public interface ChatCallback {
    void onConnected(); // 서버 연결 시 호출
    void onMessageReceived(String message); // 메시지 수신 시 호출
    void onDisconnected(String reason); // 연결 종료 시 호출
    void onError(Throwable error); // 오류 발생 시 호출
}
