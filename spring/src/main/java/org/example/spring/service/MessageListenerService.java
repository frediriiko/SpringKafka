package org.example.spring.service;

import org.example.spring.websocket.WebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {

    private final WebSocketHandler webSocketHandler;

    public MessageListenerService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(topics = "messages", groupId = "message_id")
    public void listen(String message) {
        webSocketHandler.broadcastMessage(message);
    }
}

