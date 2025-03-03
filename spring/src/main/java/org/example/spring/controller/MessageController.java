package org.example.spring.controller;

import org.example.spring.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public void sendMessage(@RequestBody MessageRequest request) {
        messageService.sendMessage(request.getMessage());
    }

    static class MessageRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

