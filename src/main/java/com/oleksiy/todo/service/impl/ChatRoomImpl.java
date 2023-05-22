package com.oleksiy.todo.service.impl;

import com.oleksiy.todo.exception.NullEntityReferenceException;
import com.oleksiy.todo.model.chat.ChatRoom;
import com.oleksiy.todo.repository.ChatRoomRepository;
import com.oleksiy.todo.service.ChatRoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatRoomImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom create(ChatRoom chatRoom) {
        if (chatRoom != null) {
            return chatRoomRepository.save(chatRoom);
        }
        throw new NullEntityReferenceException("ChatRoom cannot be 'null'");
    }

    @Override
    public ChatRoom readById(long id) {
        return chatRoomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ChatRoom with id " + id + " not found"));
    }

    @Override
    public void delete(long id) {
        chatRoomRepository.deleteById(id);
    }

}
