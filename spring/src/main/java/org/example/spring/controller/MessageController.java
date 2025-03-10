package org.example.spring.controller;

import org.example.spring.model.Message;
import org.example.spring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages/{roomId}")
    public List<Message> getChatHistory(@PathVariable String roomId) {
        log.info("🔍 Fetching chat history for room: {}", roomId);
        return messageService.getChatHistory(roomId);
    }

}

