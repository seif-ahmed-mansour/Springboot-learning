package com.demo.crud.students.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="students")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false, length = 64)
    private String name;

    @Column(unique = true, nullable = false, length = 128)
    private String student_email;

    private Integer grade;

    private Integer age;

}
