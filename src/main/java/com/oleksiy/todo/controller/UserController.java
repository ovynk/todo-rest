package com.oleksiy.todo.controller;

import com.oleksiy.todo.dto.UserResponse;
import com.oleksiy.todo.model.Role;
import com.oleksiy.todo.model.User;
import com.oleksiy.todo.service.RoleService;
import com.oleksiy.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') or isAnonymous()")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid User user) {
        user.setRole(roleService.readById(2L));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(new UserResponse(userService.create(user)), HttpStatus.CREATED);
    }

    @GetMapping("/{user_id}")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<UserResponse> readUser(@PathVariable("user_id") long id) {
        return new ResponseEntity<>(new UserResponse(userService.readById(id)), HttpStatus.OK);
    }

    @PutMapping("/{user_id}")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("user_id") long id, @RequestBody @Valid User user) {
        user.setId(id);
        user.setEmail(userService.readById(id).getEmail()); // save old email
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // may be error null
        return new ResponseEntity<>(new UserResponse(userService.update(user)), HttpStatus.OK);
    }

    @DeleteMapping( "/{user_id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable("user_id") long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

}
