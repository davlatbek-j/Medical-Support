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
public class ReceptionAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 300)
    String addressName;

    String httpUrl;

    public ReceptionAddress(String addressName, String httpUrl) {
        this.addressName = addressName;
        this.httpUrl = httpUrl;
    }
}
