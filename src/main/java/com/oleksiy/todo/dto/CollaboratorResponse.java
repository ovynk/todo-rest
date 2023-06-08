package com.oleksiy.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oleksiy.todo.model.User;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CollaboratorResponse {

    Long id;
    String firstName;

    CollaboratorResponse(User user) {
        id = user.getId();
        firstName = user.getFirstName();
    }

}
