package ru.gubenko.server1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.repository.MessageRepository;
import ru.gubenko.server1.repository.RoleRepository;
import ru.gubenko.server1.repository.UserRepository;

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
        Optional<User> opt= Optional.of(userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("user not found")));
        return opt.get();
    }
}
