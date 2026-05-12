package com.internhub.controller;

import com.internhub.dto.StudentProfileDTO;
import com.internhub.model.Student;
import com.internhub.service.ApplicationService;
import com.internhub.service.InternshipService;
import com.internhub.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final InternshipService internshipService;
    private final ApplicationService applicationService;
    private final String cvUploadDir;

    public StudentController(StudentService studentService, InternshipService internshipService, ApplicationService applicationService,
                             @Value("${internhub.upload.cv-dir:uploads/cvs}") String cvUploadDir) {
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.applicationService = applicationService;
        this.cvUploadDir = cvUploadDir;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long studentId = requireStudent(session);
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        model.addAttribute("internships", internshipService.findOpenInternships());
        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Student student = studentService.findById(requireStudent(session));
        model.addAttribute("profile", toProfileDto(student));
        return "student/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profile") StudentProfileDTO dto, BindingResult result,
                                @RequestParam("cvFile") MultipartFile cvFile, HttpSession session) {
        if (result.hasErrors()) {
            return "student/profile";
        }
        if (!cvFile.isEmpty()) {
            dto.setCvFileName(storeCv(cvFile));
        }
        Student profile = new Student();
        profile.setName(dto.getName());
        profile.setUniversity(dto.getUniversity());
        profile.setMajor(dto.getMajor());
        profile.setSkills(dto.getSkills());
        profile.setCvFileName(dto.getCvFileName());
        studentService.updateProfile(requireStudent(session), profile);
        return "redirect:/student/profile?updated";
    }

    @PostMapping("/favorites/{internshipId}")
    public String toggleFavorite(@PathVariable Long internshipId, HttpSession session) {
        studentService.toggleFavorite(requireStudent(session), internshipId);
        return "redirect:/student/dashboard";
    }

    @GetMapping("/favorites")
    public String favorites(HttpSession session, Model model) {
        Student student = studentService.findById(requireStudent(session));
        model.addAttribute("favorites", student.getFavoriteInternships());
        return "student/favorites";
    }

    @PostMapping("/apply/{internshipId}")
    public String apply(@PathVariable Long internshipId, HttpSession session) {
        applicationService.apply(requireStudent(session), internshipId);
        return "redirect:/student/applications";
    }

    @GetMapping("/applications")
    public String applications(HttpSession session, Model model) {
        Student student = studentService.findById(requireStudent(session));
        model.addAttribute("applications", applicationService.findByStudent(student).stream()
                .map(applicationService::toDto)
                .collect(Collectors.toList()));
        return "student/applications";
    }

    @PostMapping("/applications/{applicationId}/withdraw")
    public String withdraw(@PathVariable Long applicationId, HttpSession session) {
        applicationService.withdraw(requireStudent(session), applicationId);
        return "redirect:/student/applications";
    }

    private Long requireStudent(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            throw new IllegalArgumentException("Please login as a student");
        }
        return studentId;
    }

    private StudentProfileDTO toProfileDto(Student student) {
        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setName(student.getName());
        dto.setUniversity(student.getUniversity());
        dto.setMajor(student.getMajor());
        dto.setSkills(student.getSkills());
        dto.setCvFileName(student.getCvFileName());
        return dto;
    }

    private String storeCv(MultipartFile cvFile) {
        try {
            String originalFileName = cvFile.getOriginalFilename() == null ? "cv.pdf" : Paths.get(cvFile.getOriginalFilename()).getFileName().toString();
            String storedFileName = System.currentTimeMillis() + "-" + originalFileName.replace(" ", "-");
            Path uploadPath = Paths.get(cvUploadDir);
            Files.createDirectories(uploadPath);
            cvFile.transferTo(uploadPath.resolve(storedFileName));
            return storedFileName;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not upload CV");
        }
    }
}
