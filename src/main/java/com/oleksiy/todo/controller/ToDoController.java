package com.oleksiy.todo.controller;

import com.oleksiy.todo.dto.ToDoResponse;
import com.oleksiy.todo.model.ToDo;
import com.oleksiy.todo.model.User;
import com.oleksiy.todo.model.chat.ChatRoom;
import com.oleksiy.todo.repository.RoleRepository;
import com.oleksiy.todo.service.ChatRoomService;
import com.oleksiy.todo.service.ToDoService;
import com.oleksiy.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/todos")
@AllArgsConstructor
public class ToDoController {

    private final ToDoService todoService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #ownerId == authentication.principal.id)")
    public ResponseEntity<ToDoResponse> createToDo(@RequestBody @Valid ToDo todo,
                                                   @RequestParam("user_id") long ownerId) {
        todo.setCreatedAt(LocalDateTime.now());
        todo.setOwner(userService.readById(ownerId));
        ToDo createdToDo = todoService.create(todo);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTodo(createdToDo);
        chatRoomService.create(chatRoom);

        return new ResponseEntity<>(new ToDoResponse(createdToDo), HttpStatus.CREATED);
    }

    @GetMapping("/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwnerOrCollaborator(#id, authentication.principal.id))")
    public ResponseEntity<ToDoResponse> readToDo(@PathVariable("todo_id") long id) {
        return new ResponseEntity<>(new ToDoResponse(todoService.readById(id)), HttpStatus.OK);
    }

    @PutMapping("/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public ResponseEntity<ToDoResponse> updateToDo(@RequestBody @Valid ToDo toDo,
                                                   @PathVariable("todo_id") long todoId) {
        ToDo oldTodo = todoService.readById(todoId);
        oldTodo.setTitle(toDo.getTitle());
        return new ResponseEntity<>(new ToDoResponse(todoService.update(oldTodo)), HttpStatus.OK);
    }

    @DeleteMapping("/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public ResponseEntity<?> deleteToDo(@PathVariable("todo_id") long todoId) {
        todoService.delete(todoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<ToDoResponse>> getAllByUser(@RequestParam(name = "user_id") long userId) {
        return new ResponseEntity<>(todoService.getByUserId(userId)
                .stream()
                .map(ToDoResponse::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{todo_id}/add")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#id, authentication.principal.id))")
    public ResponseEntity<ToDoResponse> addCollaborator(@PathVariable("todo_id") long id,
                                                        @RequestParam("user_id") long userId) {
        ToDo todo = todoService.readById(id);

        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(userId));
        todo.setCollaborators(collaborators);

        return new ResponseEntity<>(new ToDoResponse(todoService.update(todo)), HttpStatus.OK);
    }

    @GetMapping("/{todo_id}/remove")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#id, authentication.principal.id))")
    public ResponseEntity<ToDoResponse> removeCollaborator(@PathVariable("todo_id") long id,
                                                           @RequestParam("user_id") long userId) {
        ToDo todo = todoService.readById(id);

        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(userId));
        todo.setCollaborators(collaborators);

        return new ResponseEntity<>(new ToDoResponse(todoService.update(todo)), HttpStatus.OK);
    }

}