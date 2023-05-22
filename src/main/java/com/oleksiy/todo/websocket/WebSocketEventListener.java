package com.oleksiy.todo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection, user: " +
                Objects.requireNonNull(event.getUser()).getName());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        logger.debug(headerAccessor.getLogin());

        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        String todoId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("todoId");
        if(username != null && todoId != null) {
            logger.info("User Disconnected: " + username + ", from socket with todoId: " + todoId);
        }
    }

}