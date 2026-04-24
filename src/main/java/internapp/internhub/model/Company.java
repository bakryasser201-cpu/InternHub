package internapp.internhub.model;

public class Company {
}
package internapp.internhub.model;

public class Company {

    private Long id;
    private String companyName;
    private String industry;
    private String location;
    private String email;
    private String phoneNumber;
  
    public Company() {
    }

    
    public Company(Long id, String companyName, String industry, String location, String email, String phoneNumber) {
        this.id = id;
        this.companyName = companyName;
        this.industry = industry;
        this.location = location;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
