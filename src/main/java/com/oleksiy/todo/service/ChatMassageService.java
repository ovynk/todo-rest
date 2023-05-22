package com.oleksiy.todo.service;

import com.oleksiy.todo.model.chat.ChatMessage;

import java.util.List;

public interface ChatMassageService {
    ChatMessage create(ChatMessage chatMessage);
    ChatMessage readById(long id);
    void delete(long id);
    List<ChatMessage> getAllByChatRoomId(long chatRoomId);
}
