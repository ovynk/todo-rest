package com.oleksiy.todo.controller;

import com.oleksiy.todo.model.Priority;
import com.oleksiy.todo.model.State;
import com.oleksiy.todo.model.Task;
import com.oleksiy.todo.service.TaskService;
import com.oleksiy.todo.service.ToDoService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ToDoService todoService;

    @GetMapping("/create/todos/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public String create(@PathVariable("todo_id") long todoId, Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id))")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") Task task, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }
        task.setTodo(todoService.readById(todoId));
        task.setState(State.NEW);
        taskService.create(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') " +
            "and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id) " +
            "and @taskServiceImpl.taskFromThisTodo(#taskId, #todoId))")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model) {
        Task task = taskService.readById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", State.values());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') " +
            "and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id) " +
            "and @taskServiceImpl.taskFromThisTodo(#taskId, #todoId))")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task")Task task, BindingResult result) {
        if (result.hasErrors()) {
            task.setTodo(todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", State.values());
            return "update-task";
        }
        task.setTodo(todoService.readById(todoId));
        task.setId(task.getId());
        taskService.update(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @toDoServiceImpl.userIsOwner(#todoId, authentication.principal.id) " +
            "and @taskServiceImpl.taskFromThisTodo(#taskId, #todoId))")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) {
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}