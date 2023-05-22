package com.oleksiy.todo.repository;

import com.oleksiy.todo.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "select * from messages where chatroom_id = ?1", nativeQuery = true)
    List<ChatMessage> getAllByChatRoomId(long chatRoomId);

}