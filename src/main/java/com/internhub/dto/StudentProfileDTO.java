package com.internhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String university;
    private String major;
    private String skills;
    private String cvFileName;
}
