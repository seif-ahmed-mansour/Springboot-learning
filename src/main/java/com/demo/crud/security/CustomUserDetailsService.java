package com.demo.crud.security;

import com.demo.crud.students.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        System.out.println(studentRepository.findByName(name));
        return studentRepository.findByName(name)
                .map(StudentPrinciple::new)
                .orElseThrow(() -> {
                    System.out.println(studentRepository.findByName(name));
                    log.warn("Failed login attempt with name: {}", name);
                    return new UsernameNotFoundException("Student not found with name: " + name);
                });
    }
}
