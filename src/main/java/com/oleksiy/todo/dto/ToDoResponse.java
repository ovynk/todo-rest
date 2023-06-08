package com.oleksiy.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oleksiy.todo.model.ToDo;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
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
        collaborators =  toDo.getCollaborators() == null
                ? new ArrayList<>()
                : toDo.getCollaborators().stream().map(CollaboratorResponse::new).collect(Collectors.toList());
    }

}