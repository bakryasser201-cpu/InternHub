package com.internhub.service;

import com.internhub.dto.ApplicationDTO;
import com.internhub.exception.ResourceNotFoundException;
import com.internhub.model.Application;
import com.internhub.model.ApplicationStatus;
import com.internhub.model.Internship;
import com.internhub.model.InternshipStatus;
import com.internhub.model.Student;
import com.internhub.repository.ApplicationRepository;
import com.internhub.repository.InternshipRepository;
import com.internhub.repository.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;

    public ApplicationService(ApplicationRepository applicationRepository, StudentRepository studentRepository, InternshipRepository internshipRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.internshipRepository = internshipRepository;
    }

    @Transactional
    public void apply(Long studentId, Long internshipId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found"));
        if (internship.getStatus() == InternshipStatus.CLOSED) {
            throw new IllegalArgumentException("This internship is closed");
        }
        if (applicationRepository.existsByStudentAndInternship(student, internship)) {
            throw new IllegalArgumentException("You already applied to this internship");
        }
        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);
        application.setAppliedDate(LocalDate.now());
        application.setStatus(ApplicationStatus.PENDING);
        applicationRepository.save(application);
    }

    @Transactional
    public void withdraw(Long studentId, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!application.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("You can withdraw only your own applications");
        }
        applicationRepository.delete(application);
    }

    @Transactional
    public void updateStatus(Long applicationId, Long companyId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!application.getInternship().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("You can manage only applications for your internships");
        }
        application.setStatus(status);
        applicationRepository.save(application);
    }

    public List<Application> findByStudent(Student student) {
        return applicationRepository.findByStudent(student);
    }

    public List<Application> findByInternship(Internship internship) {
        return applicationRepository.findByInternship(internship);
    }

    public ApplicationDTO toDto(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setAppliedDate(application.getAppliedDate());
        dto.setStatus(application.getStatus());
        dto.setStudentId(application.getStudent().getId());
        dto.setStudentName(application.getStudent().getName());
        dto.setStudentMajor(application.getStudent().getMajor());
        dto.setStudentSkills(application.getStudent().getSkills());
        dto.setCvFileName(application.getStudent().getCvFileName());
        dto.setInternshipId(application.getInternship().getId());
        dto.setInternshipTitle(application.getInternship().getTitle());
        return dto;
    }
}
