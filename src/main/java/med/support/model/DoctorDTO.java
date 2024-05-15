package med.support.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorDTO {
    Long id;
    String firstname;
    String lastname;
    String surname;
    String phone;
    String login;
    String password;
    String outline;
    String motto;
    String photoUrl;
    Set<String> specialty;
    Set<String> language;
    Set<String> achievement;
    Set<ExperienceDTO> experience;
    Set<EducationDTO> education;
    Set<ReceptionAddressDTO> receptionAddress;
    Set<ServiceDTO> service;
    Set<ContactDTO> contact;
    MultipartFile photoFile;
}
