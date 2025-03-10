package org.example.spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    @Column(nullable = false)
    private String roomId;

    public Message() {}

    public Message(@JsonProperty("author") String author,
                   @JsonProperty("content") String content,
                   @JsonProperty("timestamp") Instant timestamp,
                   @JsonProperty("roomId") String roomId) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    @Override
    public String toString() {
        return String.format("Message{author='%s', content='%s', timestamp='%s'}",
                author,
                content,
                timestamp,
                roomId);
    }

}
