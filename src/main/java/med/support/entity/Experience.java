package med.support.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String workplace;

    String beginDate;

    String endDate;

    String position;

    public Experience(String workplace, String beginDate, String endDate, String position) {
        this.workplace = workplace;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.position = position;
    }
}
