package org.example.spring.service;

import org.example.spring.model.Message;
import org.example.spring.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final MessageRepository messageRepository;

    public MessageService(KafkaTemplate<String, Message> kafkaTemplate, MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void sendMessage(Message message) {
        log.info("Saving Message in DB: {}", message.toString());
        messageRepository.save(message);


        log.info("Sending Message to kafka: {}", message.toString());
        String topic = "chat-" + message.getRoomId();

        try {
            kafkaTemplate.send(topic, message).get();
        } catch (Exception e) {
            log.error("❌ Failed to send message to Kafka, rolling back", e);
            throw new RuntimeException("Kafka send failed, rolling back transaction");
        }

    }

    public List<Message> getChatHistory(String roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
