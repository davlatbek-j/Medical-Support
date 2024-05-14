package med.support.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    Integer startYear;

    Integer endYear;

    String faculty;

    public Education(String name, Integer startYear, Integer endYear, String faculty) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.faculty = faculty;
    }
}
