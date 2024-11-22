package com.example.demo.Post.Chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트 연결 후 처리
        sessions.add(session);
        System.out.println("New session established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String username = (String) session.getAttributes().get("username");
            String sessionId = session.getId();

            // 처음 접속 시 유저명이 없으면, 메시지를 통해 유저명 설정
            if (username == null) {
                username = message.getPayload();
                session.getAttributes().put("username", username);
                session.sendMessage(new TextMessage("Welcome, " + username));
            }

            // 메시지 저장
            Message newMessage = new Message(message.getPayload(), sessionId, username);
            messageRepository.save(newMessage);

            // 모든 클라이언트에게 메시지 전송
            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(username + ": " + message.getPayload()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessions.remove(session);
    }
}

