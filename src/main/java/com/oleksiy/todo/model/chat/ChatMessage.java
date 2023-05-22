package com.oleksiy.todo.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 480, message = "Message must be maximum 480 chars")
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "sender")
    private String sender;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatroom;

}