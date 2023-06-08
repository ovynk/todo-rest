package com.oleksiy.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oleksiy.todo.model.ToDo;
import com.oleksiy.todo.model.User;
import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoResponse {

    Long id;
    String title;
    String createdAt;
    Long ownerId;
    List<CollaboratorResponse> collaborators;

    public ToDoResponse(ToDo toDo) {
        id = toDo.getId();
        title = toDo.getTitle();
        createdAt = String.valueOf(toDo.getCreatedAt());
        ownerId = toDo.getOwner().getId();
        collaborators = toDo.getCollaborators()
                .stream()
                .map(CollaboratorResponse::new)
                .collect(Collectors.toList());
    }

}