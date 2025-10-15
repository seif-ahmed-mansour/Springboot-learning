package com.demo.crud.students.repo;

import com.demo.crud.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM get_student_info(:id)", nativeQuery = true)
    List<Object[]> getStudentInfoById(@Param("id") Long id);

    Optional<Student> findByName(String name);
}
