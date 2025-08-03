package ru.gubenko.server1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gubenko.server1.exception.AccessDeniedException;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.repository.MessageRepository;
import ru.gubenko.server1.repository.RoleRepository;
import ru.gubenko.server1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(UserRepository userRepository, MessageRepository messageRepository){
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public List<Message> getUserMessages(String username){
        User user=getUserOrThrow(username);
        return messageRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    public Long getUnreadCount(String username){
        User user=getUserOrThrow(username);
        return messageRepository.countByRecipientAndIsReadFalse(user);
    }

    public void markAllAsRead(String username){
        User user=getUserOrThrow(username);
        List<Message>unreadMessages=messageRepository.findByRecipientAndIsReadFalse(user);
        messageRepository.saveAll(unreadMessages);
    }

    private User getUserOrThrow(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
    }

    public void sendMessage(String senderUserName,String recipientUserName,String content){
        User sender=getUserOrThrow(senderUserName);
        User recipient=getUserOrThrow(recipientUserName);

        Message message=new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);

        messageRepository.save(message);
    }

    private Message getMessagerOrThrow(Long id){
        return messageRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public Message getMessage(Long id,User currentUser){
        Message message=getMessagerOrThrow(id);

        if(!message.getRecipient().equals(currentUser)){
            throw new AccessDeniedException("you are not recipient");
        }

        if(!message.isRead()){
            message.setRead(true);
            messageRepository.save(message);
        }
        return message;
    }
}


