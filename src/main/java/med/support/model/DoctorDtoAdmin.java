package med.support.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// TODO    bu dto admin paneldan turib doctorni edit qilish uchun , narigi dtodan farqi bunda Set<> o'rniga
//  List<> ishlatilgan , shu list ishlatganimda @ModelAttribute bilan handle qilishda exception bermadi
public class DoctorDtoAdmin {
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
    List<String> specialty;
    List<String> language;
    List<String> achievement;
    List<ExperienceDTO> experience;
    List<EducationDTO> education;
    List<ReceptionAddressDTO> receptionAddress;
    List<ServiceDTO> service;
    List<ContactDTO> contact;
    MultipartFile photoFile;
}
