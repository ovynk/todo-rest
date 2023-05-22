package com.oleksiy.todo.service;

import com.oleksiy.todo.model.chat.ChatRoom;

public interface ChatRoomService {
    ChatRoom create(ChatRoom chatRoom);
    ChatRoom readById(long id);
    void delete(long id);
}
