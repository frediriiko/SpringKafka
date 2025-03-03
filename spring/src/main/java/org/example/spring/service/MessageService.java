package org.example.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Sending Message to kafka: {}", message);
        kafkaTemplate.send("messages", message);  // Send the message to the topic
    }
}
