package com.demo.crud.students.controller;

import com.demo.crud.students.dto.request.studentRequestDTO;
import com.demo.crud.students.model.Student;
import com.demo.crud.students.service.studentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.demo.crud.students.dto.response.studentResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/students")
public class studentController {
    private final studentService studentsvc;

    // TODO: Tasks::
    // 1] Validate request DTO manually for now, i.e., if else pattern /. DONE without doing it manually😉

    @Autowired
    public studentController(studentService studentsvc){
        this.studentsvc=studentsvc;
    }

    @GetMapping
    public ResponseEntity<List<studentResponseDTO>> getAllStudents(){
        return studentsvc.getAllStudents();
    }

    @PostMapping("/create")
    public ResponseEntity<studentResponseDTO> createStudent(@Valid @RequestBody studentRequestDTO requestDTO) {
        return studentsvc.createStudent(requestDTO);
    }

    // 2] Add GET student by ID endpoint
    @GetMapping("/{id}")
    public ResponseEntity<studentResponseDTO> getStudentById(@PathVariable(value = "id") long id){
        return studentsvc.getStudentById(id);
    }

    // 3] Add UPDATE student endpoint
    @PutMapping("/update/{id}")
    public ResponseEntity<studentResponseDTO> updateStudent(@Valid @RequestBody studentRequestDTO requestDTO,
                                                            @PathVariable(value = "id") long id){
        return studentsvc.updateStudent(requestDTO,id);
    }
    // 4] Add DELETE student endpoint
    @DeleteMapping("/{id}")
    public  ResponseEntity<studentResponseDTO> deleteStudent(@PathVariable(value = "id") long id){
        return studentsvc.deleteStudent(id);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<studentResponseDTO> getStudentInfo(@PathVariable Long id) {
        return studentsvc.getStudentInfo(id);
    }

}
