package org.example.spring.service;

import org.example.spring.model.Message;
import org.example.spring.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {
    private static final Logger logger = LoggerFactory.getLogger(MessageListenerService.class);

    private final WebSocketHandler webSocketHandler;

    public MessageListenerService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(topics = "messages", groupId = "message_id")
    public void listen(Message message) {
        logger.info("Received from Kafka: " + message.getAuthor() + " " + message.getContent() + " " + message.getTimestamp());
        webSocketHandler.broadcastMessage(message);
    }
}

