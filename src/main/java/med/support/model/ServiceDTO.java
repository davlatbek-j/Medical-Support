package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceDTO {
    String name;
    Integer price;

    public boolean isComplete() {
        return (name != null && !name.isEmpty
                ()) &&
                (price != null);
    }
}
