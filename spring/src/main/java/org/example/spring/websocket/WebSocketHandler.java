package org.example.spring.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.spring.model.Message;
import org.example.spring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    private final Map<String, Set<WebSocketSession>> roomSessions = new HashMap<>();

    public WebSocketHandler(MessageService messageService, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = extractQueryParam(session, "roomId");

        logger.info("A new User joined room '{}'", roomId);

        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        logger.info("Received message: " + message.getPayload());

        Message msg = objectMapper.readValue(message.getPayload(), Message.class);
        messageService.sendMessage(msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        roomSessions.values().forEach(sessions -> sessions.remove(session));
        logger.info("WebSocket closed: " + session.getId() + " with status " + status);
    }

    public void broadcastMessage(Message message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            Set<WebSocketSession> sessions = roomSessions.get(message.getRoomId());

            if (sessions == null || sessions.isEmpty()) {
                logger.warn(" No active WebSocket sessions for room: {}", message.getRoomId());
                return;
            }

            logger.info("Broadcasting message to {} sessions in room: {}", sessions.size(), message.getRoomId());

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            }

        } catch (IOException e) {
            logger.error("Error sending WebSocket message", e);
        }
    }

    private String extractQueryParam(WebSocketSession session, String key) {
        URI uri = session.getUri();
        if (uri == null) return null;

        return Arrays.stream(uri.getQuery().split("&"))
                .map(param -> param.split("="))
                .filter(parts -> parts.length == 2 && parts[0].equals(key))
                .map(parts -> parts[1])
                .findFirst()
                .orElse(null);
    }
}

