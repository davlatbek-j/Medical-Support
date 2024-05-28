package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperienceDTO {

    String workplace;

    String beginDate;

    String endDate;

    String position;

    public boolean isComplete() {
        return (workplace != null && !workplace.isEmpty()) &&
                (beginDate != null && !beginDate.isEmpty()) &&
                (endDate != null && !endDate.isEmpty()) &&
                (position != null && !position.isEmpty());
    }
}
