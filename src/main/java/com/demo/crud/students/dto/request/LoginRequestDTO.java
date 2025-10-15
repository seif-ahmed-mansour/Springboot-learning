package com.demo.crud.students.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "username is required")
    private String name;

    @NotBlank(message = "password is required")
    private String password;

}
