package com.internhub.service;

import com.internhub.dto.LoginRequestDTO;
import com.internhub.dto.RegisterRequestDTO;
import com.internhub.exception.ResourceNotFoundException;
import com.internhub.model.Company;
import com.internhub.model.Student;
import com.internhub.model.User;
import com.internhub.model.UserRole;
import com.internhub.repository.CompanyRepository;
import com.internhub.repository.StudentRepository;
import com.internhub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, StudentRepository studentRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        validateRoleFields(dto);

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        User savedUser = userRepository.save(user);

        if (dto.getRole() == UserRole.STUDENT) {
            Student student = new Student();
            student.setName(dto.getName());
            student.setUniversity(dto.getUniversity());
            student.setMajor(dto.getMajor());
            student.setSkills(dto.getSkills());
            student.setUser(savedUser);
            studentRepository.save(student);
        } else {
            Company company = new Company();
            company.setCompanyName(dto.getCompanyName() == null || dto.getCompanyName().isBlank() ? dto.getName() : dto.getCompanyName());
            company.setDescription(dto.getDescription());
            company.setLocation(dto.getLocation());
            company.setUser(savedUser);
            companyRepository.save(company);
        }

        return savedUser;
    }

    public User login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordMatches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        upgradePlainTextPasswordIfNeeded(user, dto.getPassword());
        return user;
    }

    public Student getStudentProfile(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
    }

    public Company getCompanyProfile(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found"));
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword != null && storedPassword.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return storedPassword != null && storedPassword.equals(rawPassword);
    }

    private void upgradePlainTextPasswordIfNeeded(User user, String rawPassword) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
        }
    }

    private void validateRoleFields(RegisterRequestDTO dto) {
        if (dto.getRole() == UserRole.STUDENT && isBlank(dto.getName())) {
            throw new IllegalArgumentException("Student name is required");
        }
        if (dto.getRole() == UserRole.COMPANY && isBlank(dto.getCompanyName())) {
            throw new IllegalArgumentException("Company name is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
