package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationDTO {
    String name;

    Integer startYear;

    Integer endYear;

    String faculty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EducationDTO that = (EducationDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(startYear, that.startYear) &&
                Objects.equals(endYear, that.endYear) &&
                Objects.equals(faculty, that.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startYear, endYear, faculty);
    }
}
