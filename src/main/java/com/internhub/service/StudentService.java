package com.internhub.service;

import com.internhub.exception.ResourceNotFoundException;
import com.internhub.model.Internship;
import com.internhub.model.Student;
import com.internhub.repository.InternshipRepository;
import com.internhub.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;

    public StudentService(StudentRepository studentRepository, InternshipRepository internshipRepository) {
        this.studentRepository = studentRepository;
        this.internshipRepository = internshipRepository;
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
    }

    @Transactional
    public Student updateProfile(Long studentId, Student profile) {
        Student student = findById(studentId);
        student.setName(profile.getName());
        student.setUniversity(profile.getUniversity());
        student.setMajor(profile.getMajor());
        student.setSkills(profile.getSkills());
        if (profile.getCvFileName() != null && !profile.getCvFileName().isBlank()) {
            student.setCvFileName(profile.getCvFileName());
        }
        return studentRepository.save(student);
    }

    @Transactional
    public void toggleFavorite(Long studentId, Long internshipId) {
        Student student = findById(studentId);
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found"));
        if (student.getFavoriteInternships().contains(internship)) {
            student.getFavoriteInternships().remove(internship);
        } else {
            student.getFavoriteInternships().add(internship);
        }
        studentRepository.save(student);
    }
}
