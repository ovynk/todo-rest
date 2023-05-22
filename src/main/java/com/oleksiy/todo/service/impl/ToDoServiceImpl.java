package com.oleksiy.todo.service.impl;

import com.oleksiy.todo.exception.NullEntityReferenceException;
import com.oleksiy.todo.model.ToDo;
import com.oleksiy.todo.repository.ToDoRepository;
import com.oleksiy.todo.service.ToDoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        if (todo != null) {
            return toDoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public ToDo readById(long id) {
        return toDoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo todo) {
        if (todo != null) {
            readById(todo.getId());
            return toDoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        toDoRepository.deleteById(id);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> toDos = toDoRepository.findAll();
        return toDos.isEmpty() ? new ArrayList<>() : toDos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> toDos = toDoRepository.getByUserId(userId);
        return toDos.isEmpty() ? new ArrayList<>() : toDos;
    }

    public boolean userIsOwner(long toDoId, long userId) {
        return readById(toDoId).getOwner().getId() == userId;
    }

    public boolean userIsOwnerOrCollaborator(long toDoId, long userId) {
        return getByUserId(userId)
                .stream()
                .anyMatch(toDo -> toDo.getId() == toDoId);
    }
}
