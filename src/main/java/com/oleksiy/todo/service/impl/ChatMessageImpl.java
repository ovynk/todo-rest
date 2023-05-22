package com.oleksiy.todo.service.impl;

import com.oleksiy.todo.exception.NullEntityReferenceException;
import com.oleksiy.todo.model.chat.ChatMessage;
import com.oleksiy.todo.repository.ChatMessageRepository;
import com.oleksiy.todo.service.ChatMassageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageImpl implements ChatMassageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage create(ChatMessage chatMessage) {
        if (chatMessage != null) {
            return chatMessageRepository.save(chatMessage);
        }
        throw new NullEntityReferenceException("Message cannot be 'null'");
    }

    @Override
    public ChatMessage readById(long id) {
        return chatMessageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Message with id " + id + " not found"));
    }

    @Override
    public void delete(long id) {
        chatMessageRepository.deleteById(id);
    }

    @Override
    public List<ChatMessage> getAllByChatRoomId(long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.getAllByChatRoomId(chatRoomId);
        return messages.isEmpty() ? new ArrayList<>() : messages;
    }

}
