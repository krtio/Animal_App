package com.example.demo.Post.Chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    private final MessageRepository messageRepository;

    @Autowired
    public ChatHistoryService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getChatHistory(String sessionId) {
        return messageRepository.findBySessionId(sessionId);  // 세션 ID로 메시지 조회
    }

    public List<Message> getUserChatHistory(String username) {
        return messageRepository.findByUsername(username);  // 사용자명으로 메시지 조회
    }
}

