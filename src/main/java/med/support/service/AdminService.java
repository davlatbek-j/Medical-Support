package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.*;
import med.support.mapper.*;
import med.support.model.*;
import med.support.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor

public class AdminService {
    private final DoctorService doctorService;
    private final PhotoService photoService;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final ReceptionAddressMapper addressMapper;
    private final ServiceMapper serviceMapper;
    private final ContactMapper contactMapper;
    private final ReceptionAddressMapper receptionAddressMapper;


    public DoctorDtoAdmin toDoctorDtoAdmin(Doctor doctor) {
        DoctorDtoAdmin doctorDtoAdmin = new DoctorDtoAdmin();
        doctorDtoAdmin.setId(doctor.getId());
        doctorDtoAdmin.setFirstname(doctor.getFirstname());
        doctorDtoAdmin.setLastname(doctor.getLastname());
        doctorDtoAdmin.setSurname(doctor.getSurname());
        doctorDtoAdmin.setPhone(doctor.getPhone());
        doctorDtoAdmin.setLogin(doctor.getLogin());
        doctorDtoAdmin.setPassword(doctor.getPassword());
        doctorDtoAdmin.setOutline(doctor.getOutline());
        doctorDtoAdmin.setMotto(doctor.getMotto());

        if (doctor.getPhoto() != null)
            doctorDtoAdmin.setPhotoUrl(doctor.getPhoto().getHttpUrl());


        doctorDtoAdmin.setSpecialty(new ArrayList<>());
        for (Speciality speciality : doctor.getSpeciality()) {
            doctorDtoAdmin.getSpecialty().add(speciality.getName());
        }

        doctorDtoAdmin.setLanguage(new ArrayList<>());
        for (Language language : doctor.getLanguage()) {
            doctorDtoAdmin.getLanguage().add(language.getName());
        }

        doctorDtoAdmin.setAchievement(new ArrayList<>());
        for (Achievement achievement : doctor.getAchievement()) {
            doctorDtoAdmin.getAchievement().add(achievement.getName());
        }

        doctorDtoAdmin.setExperience(new ArrayList<>());
        for (Experience experience : doctor.getExperience()) {
            doctorDtoAdmin.getExperience().add(experienceMapper.toDTO(experience));
        }

        doctorDtoAdmin.setEducation(new ArrayList<>());
        for (Education education : doctor.getEducation()) {
            doctorDtoAdmin.getEducation().add(educationMapper.toDto(education));
        }

        doctorDtoAdmin.setReceptionAddress(new ArrayList<>());
        for (ReceptionAddress address : doctor.getReceptionAddress()) {
            doctorDtoAdmin.getReceptionAddress().add(receptionAddressMapper.toDto(address));
        }

        doctorDtoAdmin.setService(new ArrayList<>());
        for (med.support.entity.Service service : doctor.getService()) {
            doctorDtoAdmin.getService().add(serviceMapper.getDto(service));
        }

        doctorDtoAdmin.setContact(new ArrayList<>());
        for (Contact contact : doctor.getContact()) {
            doctorDtoAdmin.getContact().add(contactMapper.toDto(contact));
        }

        return doctorDtoAdmin;
    }

    public Doctor toEntity(DoctorDtoAdmin doctorDtoAdmin) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDtoAdmin.getId());
        doctor.setFirstname(doctorDtoAdmin.getFirstname());
        doctor.setLastname(doctorDtoAdmin.getLastname());
        doctor.setSurname(doctorDtoAdmin.getSurname());
        doctor.setPhone(doctorDtoAdmin.getPhone());
        doctor.setLogin(doctorDtoAdmin.getLogin());
        doctor.setPassword(doctorDtoAdmin.getPassword());
        doctor.setOutline(doctorDtoAdmin.getOutline());
        doctor.setMotto(doctorDtoAdmin.getMotto());
        //TODO photo=null
        doctor.setPhoto(null);

        doctor.setSpeciality(new HashSet<>());
        for (String s : doctorDtoAdmin.getSpecialty()) {
            doctor.getSpeciality().add(new Speciality(s));
        }

        doctor.setLanguage(new HashSet<>());
        for (String s : doctorDtoAdmin.getLanguage()) {
            doctor.getLanguage().add(new Language(s));
        }

        doctor.setAchievement(new HashSet<>());
        if (doctorDtoAdmin.getAchievement() != null) {
            for (String s : doctorDtoAdmin.getAchievement()) {
                doctor.getAchievement().add(new Achievement(s));
            }
        }

        doctor.setExperience(new HashSet<>());
        if (doctorDtoAdmin.getExperience() != null) {
            for (ExperienceDTO experienceDTO : doctorDtoAdmin.getExperience()) {
                if (experienceDTO.getWorkplace() != null)
                    doctor.getExperience().add(experienceMapper.toEntity(experienceDTO));
            }
        }

        doctor.setEducation(new HashSet<>());
        if (doctorDtoAdmin.getEducation() != null) {
            for (EducationDTO educationDTO : doctorDtoAdmin.getEducation()) {
                if (educationDTO.getName() != null) {
                    doctor.getEducation().add(educationMapper.toEntity(educationDTO));
                }
            }
        }

        doctor.setReceptionAddress(new HashSet<>());
        if (doctorDtoAdmin.getReceptionAddress() != null) {
            for (ReceptionAddressDTO addressDTO : doctorDtoAdmin.getReceptionAddress()) {
                if (addressDTO.getAddressName() != null) {
                    doctor.getReceptionAddress().add(receptionAddressMapper.toEntity(addressDTO));
                }
            }
        }

        doctor.setService(new HashSet<>());
        if (doctorDtoAdmin.getService() != null) {
            for (ServiceDTO serviceDTO : doctorDtoAdmin.getService()) {
                if (serviceDTO.getName() != null) {
                    doctor.getService().add(serviceMapper.getEntity(serviceDTO));
                }
            }
        }

        doctor.setContact(new HashSet<>());
        if (doctorDtoAdmin.getContact() != null) {
            for (ContactDTO contactDTO : doctorDtoAdmin.getContact()) {
                if (contactDTO.getContactType() != null) {
                    doctor.getContact().add(contactMapper.toEntity(contactDTO));
                }
            }
        }
        return doctor;
    }

    public void update(String login, DoctorDtoAdmin doctorDtoAdmin) {
        Doctor byLogin = doctorRepository.findByLogin(login);
        if (byLogin == null)
            throw new RuntimeException("Login not found");

        Doctor entity = toEntity(doctorDtoAdmin);
        entity.setId(byLogin.getId());
        entity.setLogin(login);

        if ( doctorDtoAdmin.getPhotoFile()!=null &&
            !doctorDtoAdmin.getPhotoFile().getContentType().equals("application/octet-stream")) {
            Photo save = photoService.save(doctorDtoAdmin.getPhotoFile(), login);
            entity.setPhoto(save);
        }else {
            entity.setPhoto(byLogin.getPhoto());
        }
        doctorService.saveToDb(entity);
    }

}
