package com.oleksiy.todo.controller;

import com.oleksiy.todo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    @GetMapping
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/todos/all/users/" + userRepository.findByEmail(authentication.getName()).getId();
    }

}
