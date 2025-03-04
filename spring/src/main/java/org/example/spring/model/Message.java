package org.example.spring.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Message {
    private String author;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    public Message() {}

    @JsonCreator
    public Message(@JsonProperty("author") String author,
                   @JsonProperty("content") String content,
                   @JsonProperty("timestamp") String timestamp) {
        this.author = author;
        this.content = content;
        this.timestamp = Instant.parse(timestamp);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Message{author='%s', content='%s', timestamp='%s'}",
                author,
                content,
                timestamp);
    }
}

