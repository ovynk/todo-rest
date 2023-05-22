package com.oleksiy.todo.model.chat;

import com.oleksiy.todo.model.ToDo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "todo_id")
    private ToDo todo;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.REMOVE)
    private List<ChatMessage> messages;

}