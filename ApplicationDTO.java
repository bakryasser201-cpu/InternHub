package com.internhub.dto;

import com.internhub.model.ApplicationStatus;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO {

    private Long id;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private Long studentId;
    private String studentName;
    private String studentMajor;
    private String studentSkills;
    private String cvFileName;
    private Long internshipId;
    private String internshipTitle;
}
