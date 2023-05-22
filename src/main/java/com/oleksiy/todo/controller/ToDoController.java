package com.oleksiy.todo.controller;

import com.oleksiy.todo.model.Task;
import com.oleksiy.todo.model.ToDo;
import com.oleksiy.todo.model.User;
import com.oleksiy.todo.model.chat.ChatRoom;
import com.oleksiy.todo.service.ChatRoomService;
import com.oleksiy.todo.service.TaskService;
import com.oleksiy.todo.service.ToDoService;
import com.oleksiy.todo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todos")
@AllArgsConstructor
public class ToDoController {

    private final ToDoService todoService;
    private final TaskService taskService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/create/users/{owner_id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #ownerId == authentication.principal.id)")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        model.addAttribute("todo", new ToDo());
        model.addAttribute("ownerId", ownerId);
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #ownerId == authentication.principal.id)")
    public String create(@PathVariable("owner_id") long ownerId,
                         @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "create-todo";
        }
        todo.setCreatedAt(LocalDateTime.now());
        todo.setOwner(userService.readById(ownerId));
        todoService.create(todo);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTodo(todo);
        chatRoomService.create(chatRoom);

        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwnerOrCollaborator(#id, authentication.principal.id))")
    public String read(@PathVariable long id, Model model) {
        ToDo todo = todoService.readById(id);
        List<Task> tasks = taskService.getByTodoId(id);
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != todo.getOwner().getId()).collect(Collectors.toList());
        model.addAttribute("todo", todo);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", users);

        ChatRoom chatRoom = todo.getChatRoom();
        if (chatRoom != null) {
            model.addAttribute("messages", chatRoom.getMessages());
        }

        return "tasks-list";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public String update(@PathVariable("todo_id") long todoId, Model model, @PathVariable String owner_id) {
        ToDo todo = todoService.readById(todoId);
        model.addAttribute("todo", todo);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId,
                         @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) {
        if (result.hasErrors()) {
            todo.setOwner(userService.readById(ownerId));
            return "update-todo";
        }
        ToDo oldTodo = todoService.readById(todoId);
        todo.setOwner(oldTodo.getOwner());
        todo.setCollaborators(oldTodo.getCollaborators());
        todoService.update(todo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public String delete(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId) {
        todoService.delete(todoId);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/all/users/{user_id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public String getAll(@PathVariable("user_id") long userId, Model model) {
        List<ToDo> todos = todoService.getByUserId(userId);
        model.addAttribute("todos", todos);
        model.addAttribute("user", userService.readById(userId));
        return "todos-list";
    }

    @GetMapping("/{id}/add")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#id, authentication.principal.id))")
    public String addCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(userId));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return "redirect:/todos/" + id + "/tasks";
    }

    @GetMapping("/{id}/remove")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#id, authentication.principal.id))")
    public String removeCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(userId));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return "redirect:/todos/" + id + "/tasks";
    }
}