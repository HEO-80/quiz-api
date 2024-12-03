package com.example.quiz_api.controller;


import com.example.quiz_api.entity.User;
import com.example.quiz_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quiz_api.dto.UserDTO;
import lombok.RequiredArgsConstructor;



import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        //return new ResponseEntity<>(users, HttpStatus.OK);
        return ResponseEntity.ok(users);
    }



    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        //return new ResponseEntity<>(user, HttpStatus.OK);
        return ResponseEntity.ok(user);
    }


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, @RequestParam String password) {
        UserDTO createdUser = userService.createUser(userDTO, password);
        return ResponseEntity.ok(createdUser);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO, @RequestParam(required = false) String newPassword) {
        UserDTO updatedUser = userService.updateUser(id, userDTO, newPassword);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        //return ResponseEntity.noContent().build();
    }
}
