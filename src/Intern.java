package internapp.internhub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "interns")
public class Intern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String field;
    private String duration;
    private boolean isPaid;
    private double stipend;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    public Intern() {
    }

    public Intern(Long id, String title, String description, String field,
                  String duration, boolean isPaid, double stipend,
                  LocalDate startDate, LocalDate endDate, Company company) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.field = field;
        this.duration = duration;
        this.isPaid = isPaid;
        this.stipend = stipend;
        this.startDate = startDate;
        this.endDate = endDate;
        this.company = company;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean isPaid) { this.isPaid = isPaid; }

    public double getStipend() { return stipend; }
    public void setStipend(double stipend) { this.stipend = stipend; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}
