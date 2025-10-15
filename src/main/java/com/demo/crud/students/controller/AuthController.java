package com.demo.crud.students.controller;


import com.demo.crud.security.JwtService;
import com.demo.crud.students.dto.request.LoginRequestDTO;
import com.demo.crud.students.model.Student;
import com.demo.crud.students.repo.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demo.crud.students.dto.request.studentRequestDTO;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // 1] Implement registration with proper fields validation
    // 2] Implement login with JWT
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody studentRequestDTO request){
        Optional<Student> existing = studentRepository.findByName(request.getName());

        if (existing.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Name is already taken"));
        }
        Student student =new Student();
        student.setStudent_email(request.getStudent_email());
        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setGrade(request.getGrade());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            Student saved =  studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", saved.getId(),
                            "name", saved.getName(),
                            "studentEmail", saved.getStudent_email(),
                            "message", "Registration successful"
                    ));

        }catch (DataIntegrityViolationException dive) {
            // handle DB unique constraint on email (or other constraint)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already in use or constraint violated"));
        } catch (Exception e) {
            // generic fallback
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Registration failed", "error", e.getMessage()));
        }


    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            // FIX: Use request.getName() instead of request object
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
            );

            // 2) If we reach here, authentication succeeded.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3) Generate JWT for this username
            String jwt = jwtService.generateToken(request.getName());

            // 4) Return token (client should send it as Authorization: Bearer <token>)
            return ResponseEntity.ok(Map.of("token", jwt));

        } catch (AuthenticationException ex) {
            // bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        } catch (Exception e) {
            // unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed", "error", e.getMessage()));
        }
    }
}
