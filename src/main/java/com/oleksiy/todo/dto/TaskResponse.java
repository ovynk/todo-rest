package com.oleksiy.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oleksiy.todo.model.Priority;
import com.oleksiy.todo.model.State;
import com.oleksiy.todo.model.Task;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponse {

    long id;
    String name;
    Priority priority;
    long todo_id;
    State state;

    public TaskResponse(Task task) {
        id = task.getId();
        name = task.getName();
        priority = task.getPriority();
        todo_id = task.getTodo().getId();
        state = task.getState();
    }

}
