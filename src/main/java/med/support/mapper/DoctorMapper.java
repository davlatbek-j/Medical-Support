package med.support.mapper;

import lombok.RequiredArgsConstructor;
import med.support.entity.*;
import med.support.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final ReceptionAddressMapper addressMapper;
    private final ServiceMapper serviceMapper;
    private final ContactMapper contactMapper;
    private final ReceptionAddressMapper receptionAddressMapper;

    public Doctor toEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setFirstname(dto.getFirstname());
        doctor.setLastname(dto.getLastname());
        doctor.setSurname(dto.getSurname());
        doctor.setPhone(dto.getPhone());
        doctor.setLogin(dto.getLogin());
        doctor.setPassword(dto.getPassword());
        doctor.setOutline(dto.getOutline());
        doctor.setMotto(dto.getMotto());
//        doctor.setPhoto(photo);

        // set-Speciality
        doctor.setSpeciality(new HashSet<>());
        for (String s : dto.getSpecialty()) {
            doctor.getSpeciality().add(new Speciality(s));
        }

        //set-Experience
        doctor.setExperience(new HashSet<>());
        for (ExperienceDTO experienceDTO : dto.getExperience()) {
            doctor.getExperience().add(experienceMapper.toEntity(experienceDTO));
        }

        //set-Language
        doctor.setLanguage(new HashSet<>());
        for (String s : dto.getLanguage()) {
            doctor.getLanguage().add(new Language(s));
        }

        //set-Education
        doctor.setEducation(new HashSet<>());
        for (EducationDTO educationDTO : dto.getEducation()) {
            doctor.getEducation().add(educationMapper.toEntity(educationDTO));
        }

        //Set-Achievement
        doctor.setAchievement(new HashSet<>());
        for (String s : dto.getAchievement()) {
            doctor.getAchievement().add(new Achievement(s));
        }

        //set-ReceptionAddress
        doctor.setReceptionAddress(new HashSet<>());
        for (ReceptionAddressDTO receptionAddress : dto.getReceptionAddress()) {
            doctor.getReceptionAddress().add(addressMapper.toEntity(receptionAddress));
        }

        //set-Service
        doctor.setService(new HashSet<>());
        for (ServiceDTO serviceDTO : dto.getService()) {
            doctor.getService().add(serviceMapper.getEntity(serviceDTO));
        }

        //set-Contact
        doctor.setContact(new HashSet<>());
        for (ContactDTO contactDTO : dto.getContact()) {
            doctor.getContact().add(contactMapper.toEntity(contactDTO));
        }

        return doctor;
    }

    public DoctorDTO toDTO(Doctor entity) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setSurname(entity.getSurname());
        dto.setPhone(entity.getPhone());
        dto.setLogin(entity.getLogin());
        //TODO PASSWORD
        dto.setPassword(entity.getPassword());
        dto.setOutline(entity.getOutline());
        dto.setMotto(entity.getMotto());

        if (entity.getPhoto()!=null)
            dto.setPhotoUrl(entity.getPhoto().getHttpUrl());

        //set-Speciality-DTO
        dto.setSpecialty(new HashSet<>());
        for (Speciality speciality : entity.getSpeciality()) {
            dto.getSpecialty().add(speciality.getName());
        }

        //set-Experience-DTO
        dto.setExperience(new HashSet<>());
        for (Experience experience : entity.getExperience()) {
            dto.getExperience().add( experienceMapper.toDTO(experience) );
        }

        //set-Language-DTO
        dto.setLanguage(new HashSet<>());
        for (Language language : entity.getLanguage()) {
            dto.getLanguage().add( language.getName() );
        }

        //set-Education-DTO
        dto.setEducation(new HashSet<>());
        for (Education education : entity.getEducation()) {
            dto.getEducation().add(educationMapper.toDto(education));
        }

        //set-Achievement-DTO
        dto.setAchievement(new HashSet<>());
        for (Achievement achievement : entity.getAchievement()) {
            dto.getAchievement().add(achievement.getName());
        }

        //set-ReceptionAddress-DTO
        dto.setReceptionAddress(new HashSet<>());
        for (ReceptionAddress receptionAddress : entity.getReceptionAddress()) {
            dto.getReceptionAddress().add(receptionAddressMapper.toDto(receptionAddress));
        }

        //set-Service-DTO
        dto.setService(new HashSet<>());
        for (Service service : entity.getService()) {
            dto.getService().add(serviceMapper.getDto(service));
        }

        //set-Contact-DTO
        dto.setContact(new HashSet<>());
        for (Contact contact : entity.getContact()) {
            dto.getContact().add(contactMapper.toDto(contact));
        }
        return dto;
    }

    public List<DoctorDTO> getDtoListFromEntity(List<Doctor> all) {
        List<DoctorDTO> dtoList = new ArrayList<>();
        for (Doctor doctor : all)
            dtoList.add(toDTO(doctor));

        return dtoList;
    }
}

