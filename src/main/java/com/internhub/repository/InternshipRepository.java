package com.internhub.repository;

import com.internhub.model.Company;
import com.internhub.model.Internship;
import com.internhub.model.InternshipStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    List<Internship> findByStatus(InternshipStatus status);
    List<Internship> findByCompany(Company company);
}
