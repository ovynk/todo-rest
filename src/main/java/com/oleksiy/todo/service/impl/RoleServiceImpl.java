package com.oleksiy.todo.service.impl;

import com.oleksiy.todo.exception.NullEntityReferenceException;
import com.oleksiy.todo.model.Role;
import com.oleksiy.todo.repository.RoleRepository;
import com.oleksiy.todo.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        if (role != null) {
            return roleRepository.save(role);
        }
        throw new NullEntityReferenceException("Role cannot be 'null'");
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    @Override
    public Role update(Role role) {
        if (role != null) {
            readById(role.getId());
            return roleRepository.save(role);
        }
        throw new NullEntityReferenceException("Role cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}
