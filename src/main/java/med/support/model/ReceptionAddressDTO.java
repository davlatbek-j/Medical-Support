package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceptionAddressDTO {

    String addressName;

    String httpUrl;

    public boolean isComplete() {
        return (addressName != null && !addressName.isEmpty()) &&
                (httpUrl != null && !httpUrl.isEmpty());
    }
}
