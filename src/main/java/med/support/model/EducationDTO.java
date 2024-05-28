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

    public boolean isComplete() {
        return (name != null && !name.isEmpty()) &&
                (startYear != null) &&
                (endYear != null) &&
                (faculty != null && !faculty.isEmpty());
    }
}
