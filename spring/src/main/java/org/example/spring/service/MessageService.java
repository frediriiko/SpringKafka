package org.example.spring.service;

import org.example.spring.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public MessageService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Message message) {
        log.info("Sending Message to kafka: {}", message.toString());
        kafkaTemplate.send("messages", message);
    }
}
