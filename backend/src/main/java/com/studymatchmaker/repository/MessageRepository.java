package com.studymatchmaker.repository;

import com.studymatchmaker.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
            SELECT m
            FROM Message m
            WHERE (m.sender.id = :currentUserId AND m.receiver.id = :friendId)
               OR (m.sender.id = :friendId AND m.receiver.id = :currentUserId)
            ORDER BY m.createdAt ASC
            """)
    List<Message> findConversation(
            @Param("currentUserId") Long currentUserId,
            @Param("friendId") Long friendId
    );

    long countByReceiverIdAndReadAtIsNull(Long receiverId);

    @Modifying
    @Query("""
            UPDATE Message m
            SET m.readAt = CURRENT_TIMESTAMP
            WHERE m.receiver.id = :currentUserId
              AND m.sender.id = :friendId
              AND m.readAt IS NULL
            """)
    int markConversationAsRead(
            @Param("currentUserId") Long currentUserId,
            @Param("friendId") Long friendId
    );
}