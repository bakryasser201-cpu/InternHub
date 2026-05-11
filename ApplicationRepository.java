package com.internhub.repository;

import com.internhub.model.Application;
import com.internhub.model.Internship;
import com.internhub.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(Student student);
    List<Application> findByInternship(Internship internship);
    boolean existsByStudentAndInternship(Student student, Internship internship);
    Optional<Application> findByStudentAndInternship(Student student, Internship internship);
}
