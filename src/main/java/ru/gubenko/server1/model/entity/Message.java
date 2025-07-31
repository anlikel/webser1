package ru.gubenko.server1.model.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long Id;

    private String content;
    private LocalDateTime createdAt;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
