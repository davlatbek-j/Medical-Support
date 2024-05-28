package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactDTO {

    String contactType;

    String value;

    public boolean isComplete() {
        return (contactType != null && !contactType.isEmpty()) &&
                (value != null && !value.isEmpty());
    }
}
