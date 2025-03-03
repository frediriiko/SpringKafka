package org.example.spring.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        logger.info("New WebSocket connection: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        logger.info("Received message: " + message.getPayload());
        broadcastMessage(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        logger.info("WebSocket closed: " + session.getId() + " with status " + status);
    }

    // ✅ NEW METHOD: Broadcast message to all connected clients
    public void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    logger.error("Error sending WebSocket message", e);
                }
            }
        }
    }
}

