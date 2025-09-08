package com.demo.crud.students.service;


import com.demo.crud.students.model.Student;
import com.demo.crud.students.model.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.demo.crud.students.dto.response.studentResponseDTO;
import com.demo.crud.students.dto.request.studentRequestDTO;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class studentService {

    private final StudentRepository studentRepo;

    // TODO: Tasks::
    // 1] Return a standard response, like so:
    /**
     * {
     *     "code": "0",
     *     "status": "Successful",
     *     "message": "Operation completed successfully",
     *     "data": [] or {}, etc...
     * }
     */

    // 2] Configure the application to only return non-null fields in any response
    // Use spring boot properties to do that.

    // 3]


    //get all students
    public ResponseEntity<List<studentResponseDTO>> getAllStudents(){
        List<Student> students= studentRepo.findAll();

        List<studentResponseDTO> responseList = students.stream()
                .map(student->{
                    studentResponseDTO response= new studentResponseDTO();
                    response.setName(student.getName());
                    response.setGrade(student.getGrade());
                    response.setCode("0");
                    response.setStatus("SUCCESS");
                    response.setMessage("User retrieved");
                    response.setHttpStatus(HttpStatus.OK);
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    // Adding student(create)

    public ResponseEntity<studentResponseDTO> createStudent(studentRequestDTO request, BindingResult bindingResult) {
        final studentResponseDTO responseToClient = new studentResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();

                responseToClient.setCode("-1");
                responseToClient.setStatus("Failed");
                responseToClient.setMessage(errorMessage);
                responseToClient.setHttpStatus(HttpStatus.BAD_REQUEST);

                return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
            }

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

        } catch (Exception e) {
            responseToClient.setCode("-1");
            responseToClient.setStatus("Failed");
            responseToClient.setMessage("Unexpected error occurred: " + e.getMessage());
            responseToClient.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(responseToClient.getHttpStatus()).body(responseToClient);
        }
    }

    public ResponseEntity <studentResponseDTO> getStudentById(long id){
        final studentResponseDTO response= new studentResponseDTO();
        try{
            Optional<Student> studentopt= studentRepo.findById(id);

            if (studentopt.isEmpty()){
                response.setCode("-1");
                response.setStatus("Failed");
                response.setMessage("Student not found");
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                return ResponseEntity.status(response.getHttpStatus()).body(response);

            }

            Student student= studentopt.get();

            response.setHttpStatus(HttpStatus.OK);
            response.setStatus("SUCCESS");
            response.setCode("0");
            response.setMessage("Student found");
            response.setName(student.getName());
            response.setGrade(student.getGrade() != null && student.getGrade() != 0 ? student.getGrade() : null);
            return ResponseEntity.status(response.getHttpStatus()).body(response);

        }catch (Exception e){
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("Unexpected error occurred: " + e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(response.getHttpStatus()).body(response);
        }
    }

    public ResponseEntity<studentResponseDTO> updateStudent(studentRequestDTO requestDTO,
                                                            long id,
                                                            BindingResult bindingResult){
        final studentResponseDTO response = new studentResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();

                response.setCode("-1");
                response.setStatus("Failed");
                response.setMessage(errorMessage);
                response.setHttpStatus(HttpStatus.BAD_REQUEST);

                return ResponseEntity.status(response.getHttpStatus()).body(response);
            }

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

            response.setName(existingStudent.getName());
            response.setGrade(existingStudent.getGrade());

            return ResponseEntity.status(response.getHttpStatus()).body(response);

        } catch (Exception e) {
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("Unexpected error occurred: " + e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(response.getHttpStatus()).body(response);
        }
    }

    public  ResponseEntity<studentResponseDTO> deleteStudent(long id){
        final studentResponseDTO response = new studentResponseDTO();

        try {
            if (!studentRepo.existsById(id)){
                response.setCode("-1");
                response.setStatus("Failed");
                response.setMessage("User not found");
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                return ResponseEntity.status(response.getHttpStatus()).body(response);

            }
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


        }catch (Exception e){
            response.setCode("-1");
            response.setStatus("Failed");
            response.setMessage("Unexpected error occurred: " + e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(response.getHttpStatus()).body(response);

        }
    }
}
