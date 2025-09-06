package com.demo.crud.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String name;
    private String email;
    private int age;
}
