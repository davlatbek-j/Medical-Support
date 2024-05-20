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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperienceDTO that = (ExperienceDTO) o;
        return Objects.equals(workplace, that.workplace) &&
                Objects.equals(beginDate, that.beginDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workplace, beginDate, endDate, position);
    }
}
