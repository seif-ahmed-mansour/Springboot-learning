package com.demo.crud.security;

import com.demo.crud.students.model.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record StudentPrinciple(Student studentPrinciple) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER_ROLE"));
    }

    @Override
    public String getPassword() {
        return studentPrinciple().getPassword();
    }

    @Override
    public String getUsername() {
        return studentPrinciple.getName();
    }
}
