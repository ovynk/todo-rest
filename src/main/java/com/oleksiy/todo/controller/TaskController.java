package com.oleksiy.todo.controller;

import com.oleksiy.todo.dto.TaskResponse;
import com.oleksiy.todo.model.Priority;
import com.oleksiy.todo.model.State;
import com.oleksiy.todo.model.Task;
import com.oleksiy.todo.model.ToDo;
import com.oleksiy.todo.service.TaskService;
import com.oleksiy.todo.service.ToDoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.CollationKey;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ToDoService todoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid Task task,
                                               @RequestParam("todo_id") long todoId) {
        task.setTodo(todoService.readById(todoId));
        task.setState(State.NEW);
        return new ResponseEntity<>(new TaskResponse(taskService.create(task)), HttpStatus.CREATED);
    }

    @GetMapping("/{task_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @taskServiceImpl.userIsOwnerOrCollaborator(#taskId, authentication.principal.id))")
    public ResponseEntity<TaskResponse> readTask(@PathVariable("task_id") long taskId) {
        return new ResponseEntity<>(new TaskResponse(taskService.readById(taskId)), HttpStatus.OK);
    }

    @PutMapping("/{task_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') " +
            "and @taskServiceImpl.taskFromThisTodo(#taskId, #todoId))" +
            "and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id)")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody @Valid Task task,
                                                   @PathVariable("task_id") long taskId,
                                                   @RequestParam("todo_id") long todoId) {
        Task oldTask = taskService.readById(taskId);
        oldTask.setName(task.getName());
        oldTask.setPriority(task.getPriority());
        oldTask.setState(task.getState());

        return new ResponseEntity<>(new TaskResponse(taskService.update(task)), HttpStatus.OK);
    }

    @DeleteMapping("/{task_id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @taskServiceImpl.taskFromThisTodo(#taskId, #todoId))" +
            "and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id) ")
    public ResponseEntity<?> deleteTask(@PathVariable("task_id") long taskId,
                         @RequestParam("todo_id") long todoId) {
        taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwnerOrCollaborator(#todoId, authentication.principal.id))")
    public ResponseEntity<List<TaskResponse>> getAllByToDo(@RequestParam("todo_id") long todoId) {
        return new ResponseEntity<>(taskService.getByTodoId(todoId)
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

}