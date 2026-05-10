package com.internhub.service;

import com.internhub.dto.InternshipDTO;
import com.internhub.exception.ResourceNotFoundException;
import com.internhub.model.Company;
import com.internhub.model.Internship;
import com.internhub.model.InternshipStatus;
import com.internhub.repository.CompanyRepository;
import com.internhub.repository.InternshipRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final CompanyRepository companyRepository;

    public InternshipService(InternshipRepository internshipRepository, CompanyRepository companyRepository) {
        this.internshipRepository = internshipRepository;
        this.companyRepository = companyRepository;
    }

    public List<Internship> findOpenInternships() {
        return internshipRepository.findByStatus(InternshipStatus.OPEN);
    }

    public List<Internship> findByCompany(Company company) {
        return internshipRepository.findByCompany(company);
    }

    public Internship findById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found"));
    }

    @Transactional
    public Internship create(Long companyId, InternshipDTO dto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Internship internship = new Internship();
        copyDtoToEntity(dto, internship);
        internship.setStatus(InternshipStatus.OPEN);
        internship.setCompany(company);
        return internshipRepository.save(internship);
    }

    @Transactional
    public Internship update(Long internshipId, Long companyId, InternshipDTO dto) {
        Internship internship = findCompanyInternship(internshipId, companyId);
        copyDtoToEntity(dto, internship);
        internship.setStatus(dto.getStatus() == null ? internship.getStatus() : dto.getStatus());
        return internshipRepository.save(internship);
    }

    @Transactional
    public void delete(Long internshipId, Long companyId) {
        Internship internship = findCompanyInternship(internshipId, companyId);
        internshipRepository.delete(internship);
    }

    @Transactional
    public void close(Long internshipId, Long companyId) {
        Internship internship = findCompanyInternship(internshipId, companyId);
        internship.setStatus(InternshipStatus.CLOSED);
        internshipRepository.save(internship);
    }

    public InternshipDTO toDto(Internship internship) {
        InternshipDTO dto = new InternshipDTO();
        dto.setId(internship.getId());
        dto.setTitle(internship.getTitle());
        dto.setDescription(internship.getDescription());
        dto.setLocation(internship.getLocation());
        dto.setSalary(internship.getSalary());
        dto.setRequiredSkills(internship.getRequiredSkills());
        dto.setRequiredMajor(internship.getRequiredMajor());
        dto.setDeadline(internship.getDeadline());
        dto.setStatus(internship.getStatus());
        dto.setCompanyId(internship.getCompany().getId());
        dto.setCompanyName(internship.getCompany().getCompanyName());
        return dto;
    }

    private Internship findCompanyInternship(Long internshipId, Long companyId) {
        Internship internship = findById(internshipId);
        if (!internship.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("You can manage only your own internships");
        }
        return internship;
    }

    private void copyDtoToEntity(InternshipDTO dto, Internship internship) {
        internship.setTitle(dto.getTitle());
        internship.setDescription(dto.getDescription());
        internship.setLocation(dto.getLocation());
        internship.setSalary(dto.getSalary());
        internship.setRequiredSkills(dto.getRequiredSkills());
        internship.setRequiredMajor(dto.getRequiredMajor());
        internship.setDeadline(dto.getDeadline());
    }
}
