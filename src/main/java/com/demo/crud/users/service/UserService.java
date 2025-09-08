package com.demo.crud.users.service;

import com.demo.crud.users.dto.request.CreateUserDTO;
import com.demo.crud.users.dto.response.UserResponseDTO;
import com.demo.crud.storage.InMemoryStorage;
import com.demo.crud.storage.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private InMemoryStorage<User> inMemoryStorage;

    public ResponseEntity<UserResponseDTO> getUserById(Long userId) {
        UserResponseDTO responseToClient = new UserResponseDTO();
        Optional<User> userOpt = inMemoryStorage.getById(userId);

        if (userOpt.isEmpty()) {
            responseToClient.setCode("-1");
            responseToClient.setStatus("Failed");
            responseToClient.setMessage("User not found");
            responseToClient.setHttpStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
        }

        User user = userOpt.get();
        responseToClient.setId(user.getId());
        responseToClient.setName(user.getName());
        responseToClient.setEmail(user.getEmail());
        responseToClient.setCode("0");
        responseToClient.setStatus("SUCCESS");
        responseToClient.setMessage("User found successfully");
        responseToClient.setHttpStatus(HttpStatus.OK);

        return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
    }

    public ResponseEntity<UserResponseDTO> createUser(CreateUserDTO request) {
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
        User savedUser = inMemoryStorage.create(newUser);
        UserResponseDTO responseToClient = new UserResponseDTO();
        responseToClient.setId(savedUser.getId());
        responseToClient.setName(savedUser.getName());
        responseToClient.setEmail(savedUser.getEmail());
        responseToClient.setCode("0");
        responseToClient.setStatus("SUCCESS");
        responseToClient.setMessage("User created successfully");
        responseToClient.setHttpStatus(HttpStatus.CREATED); // Use CREATED for new resources

        return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
    }

    public ResponseEntity<UserResponseDTO> updateUser(Long userId, CreateUserDTO request) {
        UserResponseDTO responseToClient = new UserResponseDTO();
        Optional<User> userOpt = inMemoryStorage.getById(userId);

        if (userOpt.isEmpty()) {
            responseToClient.setCode("-1");
            responseToClient.setStatus("Failed");
            responseToClient.setMessage("User not found");
            responseToClient.setHttpStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
        }

        User existingUser = userOpt.get();
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setAge(request.getAge());

        User savedUser = inMemoryStorage.update(userId, existingUser);

        responseToClient.setId(savedUser.getId());
        responseToClient.setName(savedUser.getName());
        responseToClient.setEmail(savedUser.getEmail());
        responseToClient.setCode("0");
        responseToClient.setStatus("SUCCESS");
        responseToClient.setMessage("User updated successfully");
        responseToClient.setHttpStatus(HttpStatus.OK);

        return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
    }

    public ResponseEntity<UserResponseDTO> deleteUser(Long userId) {
        UserResponseDTO responseToClient = new UserResponseDTO();

        if (!inMemoryStorage.exists(userId)) {
            responseToClient.setCode("-1");
            responseToClient.setStatus("Failed");
            responseToClient.setMessage("User not found");
            responseToClient.setHttpStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
        }

        boolean deleted = inMemoryStorage.delete(userId);

        if (deleted) {
            responseToClient.setCode("0");
            responseToClient.setStatus("SUCCESS");
            responseToClient.setMessage("User deleted successfully");
            responseToClient.setHttpStatus(HttpStatus.OK);
        } else {
            responseToClient.setCode("-1");
            responseToClient.setStatus("Failed");
            responseToClient.setMessage("Failed to delete user");
            responseToClient.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = inMemoryStorage.getAll();

        List<UserResponseDTO> responseList = users.stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setCode("0");
                    dto.setStatus("SUCCESS");
                    dto.setMessage("User retrieved");
                    dto.setHttpStatus(HttpStatus.OK);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }


}