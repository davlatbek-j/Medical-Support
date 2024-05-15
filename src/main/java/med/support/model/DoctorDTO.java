package med.support.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import med.support.enums.UserState;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorDTO {
    Long id;
    Long chatId;
    String firstname;
    String lastname;
    String surname;
    String phone;
    String login;
    String password;
    String outline;
    String motto;
    UserState userState;
    String photoUrl;
    Set<String> specialty;
    Set<String> language;
    Set<String> achievement;
    Set<ExperienceDTO> experience;
    Set<EducationDTO> education;
    Set<ReceptionAddressDTO> receptionAddress;
    Set<ServiceDTO> service;
    Set<ContactDTO> contact;
}
