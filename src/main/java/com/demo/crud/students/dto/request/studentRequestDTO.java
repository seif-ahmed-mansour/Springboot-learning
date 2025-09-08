package com.demo.crud.students.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class studentRequestDTO {

    @NotNull(message = "Student name is required")
    @Size(min = 2, message = "Student name must be at least 2 characters long")
    @Size(max = 64, message = "Student name must be <= 64 characters")
    private String name;

    @NotBlank(message = "Student email is required")
    @Email(message = "Must be a valid email")

    private String student_email;

    @NotNull(message = "Student grade cannot be null")
    @Positive(message = "Student grade must be a positive number")
    private Integer grade;
    @NotNull(message = "Student age cannot be null")
    @Positive(message = "Student age must be a positive number")
    private Integer age;
}
