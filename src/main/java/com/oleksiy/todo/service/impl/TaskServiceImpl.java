package com.oleksiy.todo.service.impl;

import com.oleksiy.todo.exception.NullEntityReferenceException;
import com.oleksiy.todo.model.Task;
import com.oleksiy.todo.repository.TaskRepository;
import com.oleksiy.todo.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public Task create(Task task) {
        if (task != null) {
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task update(Task task) {
        if (task != null) {
            readById(task.getId());
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    public boolean taskFromThisTodo(long taskId, long todoId) {
        return readById(taskId).getTodo().getId() == todoId;
    }
}
