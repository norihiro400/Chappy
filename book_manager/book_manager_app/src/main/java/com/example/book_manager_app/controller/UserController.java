package com.example.book_manager_app.controller;

import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/add";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public String addUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/edit";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String userDetail(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/detail";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String editUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}