package internapp.internhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

@Entity
// el @getter w @setter dol by3mlo getters w setters l kol el variables m3 nfshom
@Getter
@Setter
//@NoArgsConstructor d by3ml Default Constructor (katbna 3shan JPA t3rf t3ml object mn student w t2ra el data mn el database w tehothom gwaha)
@NoArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;
    private String university;
    private String major;

    //el id kda kda byt3ml by default
    public Student( String name, String university, String major) {

        this.name = name;
        this.university = university;
        this.major = major;
    }
}
