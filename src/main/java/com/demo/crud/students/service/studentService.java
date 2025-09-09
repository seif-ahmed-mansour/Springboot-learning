package com.demo.crud.students.service;


import com.demo.crud.students.dto.response.StudentDataDTO;
import com.demo.crud.students.model.Student;
import com.demo.crud.students.model.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.demo.crud.students.dto.response.studentResponseDTO;
import com.demo.crud.students.dto.request.studentRequestDTO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class studentService {

    private final StudentRepository studentRepo;

    // TODO: Tasks::
    // 1] Return a standard response, like so:  //DONE✅

    /**
     * {
     * "code": "0",
     * "status": "Successful",
     * "message": "Operation completed successfully",
     * "data": [] or {}, etc...
     * }
     */

    // 2] Configure the application to only return non-null fields in any response
    // Use spring boot properties to do that. DONE✅

    // 3] Implement a global exception handler and handle the following exceptions: DONE✅
    // - Data constraint violation exception
    // - Illegal argument exception
    // - Method not allowed exception
    // - Runtime exception


    //get all students
    public ResponseEntity<List<studentResponseDTO>> getAllStudents() {
        List<Student> students = studentRepo.findAll();
        StudentDataDTO data = new StudentDataDTO();
        List<studentResponseDTO> responseList = students.stream()
                .map(student -> {
                    studentResponseDTO response = new studentResponseDTO();
                    data.setName(student.getName());
                    data.setGrade(student.getGrade());
                    response.setCode("0");
                    response.setStatus("SUCCESS");
                    response.setMessage("User retrieved");
                    response.setHttpStatus(HttpStatus.OK);
                    response.setData(data);
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    // Adding student(create)
    public ResponseEntity<studentResponseDTO> createStudent(studentRequestDTO request) {
        final studentResponseDTO responseToClient = new studentResponseDTO();
        Student student = new Student();
        student.setName(request.getName());
        student.setStudent_email(request.getStudent_email());
        student.setAge(request.getAge());
        student.setGrade(request.getGrade());

        studentRepo.save(student);

        responseToClient.setCode("0");
        responseToClient.setStatus("Success");
        responseToClient.setMessage("Student created successfully");
        responseToClient.setHttpStatus(HttpStatus.OK);

        return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);


    }

    public ResponseEntity<studentResponseDTO> getStudentById(long id) {
        final studentResponseDTO response = new studentResponseDTO();
        Optional<Student> studentopt = studentRepo.findById(id);

        if (studentopt.isEmpty()) {
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("Student not found");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getHttpStatus()).body(response);

        }

        Student student = studentopt.get();
        StudentDataDTO data = new StudentDataDTO();

        response.setHttpStatus(HttpStatus.OK);
        response.setStatus("SUCCESS");
        response.setCode("0");
        response.setMessage("Student found");
        data.setName(student.getName());
        data.setGrade(student.getGrade() != null && student.getGrade() != 0 ? student.getGrade() : null);
        response.setData(data);

        return ResponseEntity.status(response.getHttpStatus()).body(response);

    }

    public ResponseEntity<studentResponseDTO> updateStudent(studentRequestDTO requestDTO, long id) {
        final studentResponseDTO response = new studentResponseDTO();


        Optional<Student> studentOpt = studentRepo.findById(id);
        if (studentOpt.isEmpty()) {
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("There is no student to update");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getHttpStatus()).body(response);
        }

        Student existingStudent = studentOpt.get();
        existingStudent.setName(requestDTO.getName());
        existingStudent.setStudent_email(requestDTO.getStudent_email());
        existingStudent.setGrade(requestDTO.getGrade());
        existingStudent.setAge(requestDTO.getAge());

        studentRepo.save(existingStudent);
        response.setHttpStatus(HttpStatus.OK);
        response.setStatus("SUCCESS");
        response.setCode("0");
        response.setMessage("Student updated successfully");
        StudentDataDTO data = new StudentDataDTO();
        data.setName(existingStudent.getName());
        data.setGrade(existingStudent.getGrade());
        response.setData(data);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    public ResponseEntity<studentResponseDTO> deleteStudent(long id) {
        final studentResponseDTO response = new studentResponseDTO();
        studentRepo.deleteById(id);
        if (!studentRepo.existsById(id)) {
            response.setCode("0");
            response.setStatus("SUCCESS");
            response.setMessage("User deleted successfully");
            response.setHttpStatus(HttpStatus.OK);
        } else {
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("Failed to delete user");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}