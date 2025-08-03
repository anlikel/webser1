package ru.gubenko.server1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.model.entity.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Page<Message> findByRecipientOrderByCreatedAtDesc(User recipient, Pageable pageable);
    List<Message> findByRecipientAndIsReadFalse(User recipient);
    long countByRecipientAndIsReadFalse(User recipient);
}
