package com.demo.crud.users.controller;

import com.demo.crud.users.dto.request.CreateUserDTO;
import com.demo.crud.users.dto.response.UserResponseDTO;
import com.demo.crud.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    // 1] Get user by ID
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(value = "id") Long id) {
        return userService.getUserById(id);
    }
    // 2] Create user

    @PostMapping("create")
    public ResponseEntity<UserResponseDTO> create(@RequestBody CreateUserDTO request) {
        return userService.createUser(request);
    }

    // 3] Update user

    @PutMapping("update/{id}")
    public ResponseEntity<UserResponseDTO> update(@RequestBody CreateUserDTO request,
                                                  @PathVariable(value = "id") Long id){
        return userService.updateUser(id,request);

    }
    // 4] Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    // 5] Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
