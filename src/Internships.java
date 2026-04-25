package internapp.internhub.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Internships {

    public static List<Intern> getSampleInternships() {

        Company google = new Company(1L, "Google", "Technology", "Cairo", "hr@google.com", "01000000001");
        Company vodafone = new Company(2L, "Vodafone", "Telecommunications", "Giza", "hr@vodafone.com", "01000000002");
        Company amazon = new Company(3L, "Amazon", "E-Commerce", "Alexandria", "hr@amazon.com", "01000000003");
//i used AI to add these content
        Intern i1 = new Intern(1L, "Backend Developer Intern",
                "Work on REST APIs using Spring Boot",
                "Software Engineering", "3 months", true, 3000,
                LocalDate.of(2025, 7, 1), LocalDate.of(2025, 9, 30), google);

        Intern i2 = new Intern(2L, "Network Engineer Intern",
                "Support the core network infrastructure team",
                "Telecommunications", "2 months", true, 2000,
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 7, 31), vodafone);

        Intern i3 = new Intern(3L, "Data Analyst Intern",
                "Analyze customer behavior data using Python",
                "Data Science", "4 months", false, 0,
                LocalDate.of(2025, 8, 1), LocalDate.of(2025, 11, 30), amazon);

        return Arrays.asList(i1, i2, i3);
    }
}
