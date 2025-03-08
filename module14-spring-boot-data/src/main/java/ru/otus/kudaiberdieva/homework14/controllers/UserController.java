package ru.otus.kudaiberdieva.homework14.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.kudaiberdieva.homework14.dtos.UserDTO;
import ru.otus.kudaiberdieva.homework14.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
