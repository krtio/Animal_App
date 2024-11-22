package com.example.demo.Post.Chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private final ChatHistoryService chatHistoryService;

    @Autowired
    public ChatController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    // 세션 ID로 채팅 히스토리 조회
    @GetMapping("/chat/history")
    public List<Message> getChatHistory(@RequestParam String sessionId) {
        return chatHistoryService.getChatHistory(sessionId);
    }

    // 사용자명으로 채팅 히스토리 조회
    @GetMapping("/chat/userHistory")
    public List<Message> getUserChatHistory(@RequestParam String username) {
        return chatHistoryService.getUserChatHistory(username);
    }
}

