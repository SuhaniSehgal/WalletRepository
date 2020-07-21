package com.example.demospringboot.controller;

import com.example.demospringboot.entity.User;
import com.example.demospringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for User
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {

        return ResponseEntity.ok().body(userService.addUser(user));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        user.setUserId(id);
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

}
