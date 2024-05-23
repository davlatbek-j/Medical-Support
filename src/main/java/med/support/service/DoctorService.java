package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.Doctor;
import med.support.entity.Language;
import med.support.entity.Photo;
import med.support.entity.Speciality;
import med.support.enums.UserState;
import med.support.mapper.DoctorMapper;
import med.support.model.ApiResponse;
import med.support.model.DoctorDTO;
import med.support.model.LoginDTO;
import med.support.repository.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final LanguageService languageService;
    private final SpecialityService specialityService;
    private final PhotoService photoService;

    //Dto'dan entity yasab db'ga saqlash uchun
    public ResponseEntity<ApiResponse> save(DoctorDTO doctorDTO) {

        try {
            Doctor doctor = saveToDb(doctorMapper.toEntity(doctorDTO));
            return ResponseEntity.ok().body(new ApiResponse(201, "Doctor Saved", doctorMapper.toDTO(doctor)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Language va Speciality ni avval db saqlab keyin , saqlangan entitylarni doctorga set qilish
    public Doctor saveToDb(Doctor doctor) {
        Set<Language> savedLang = new HashSet<>();
        for (Language language : doctor.getLanguage()) {
            savedLang.add(languageService.save(language));
        }
        doctor.setLanguage(savedLang);
        Set<Speciality> savedSpecial = new HashSet<>();
        for (Speciality speciality : doctor.getSpeciality()) {
            savedSpecial.add(specialityService.save(speciality));
        }
        doctor.setSpeciality(savedSpecial);
        return doctorRepository.save(doctor);
    }

    //Logini login bo'lgan doctorga , photoni set qilish
    public ResponseEntity<ApiResponse> setDoctorPhoto(String login, MultipartFile photo) {
        Photo save = photoService.save(photo, login);
        Doctor doctor = doctorRepository.findByLogin(login);
        doctor.setPhoto(save);
        doctorRepository.save(doctor);
        return ResponseEntity.ok().body(new ApiResponse(200, "Doctor Updated", doctorMapper.toDTO(doctor)));
    }

    //logini berilgan doctorni photosini qaytarish
    public ResponseEntity<byte[]> getPhoto(String login) {
        try {
            Doctor byLogin = doctorRepository.findByLogin(login);

            Photo photo = doctorRepository.findByLogin(login).getPhoto();

            Path imagePath = Paths.get(photo.getSystemPath());
            byte[] imageBytes = Files.readAllBytes(imagePath);

            if (photo.getType().equals("image/png"))
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
            else if (photo.getType().equals("image/jpeg"))
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DoctorDTO> getAllDto() {
        List<Doctor> all = doctorRepository.findAll();
        return doctorMapper.getDtoListFromEntity(all);
    }

    public ResponseEntity<ApiResponse> findByLogin(String login) {
        Doctor byLogin = doctorRepository.findByLogin(login);
        if (byLogin != null) {
            return ResponseEntity.ok().body(new ApiResponse(200, "Doctor Found", doctorMapper.toDTO(byLogin)));
        }
        return ResponseEntity.notFound().build();
    }
    public  Photo findPhotoByLogin(String login) {
        Long photoId = doctorRepository.findPhotoIdByDoctorLogin(login);
        Photo byId = photoService.findById(photoId);
        return  byId;
    }

    public ResponseEntity<ApiResponse> update(String login,Long chatId, UserState state, DoctorDTO doctorDTO) {
        Doctor byLogin = doctorRepository.findByLogin(login);
        if (byLogin == null)
            return ResponseEntity.notFound().build();
        Doctor entity = doctorMapper.toEntity(doctorDTO);
        entity.setId(byLogin.getId());
        entity.setLogin(byLogin.getLogin());
        entity.setPassword(byLogin.getPassword());
        entity.setChatId(byLogin.getChatId());
        entity.setState(byLogin.getState());
        entity.setPhoto(byLogin.getPhoto());



        saveToDb(entity);
        return ResponseEntity.ok().body(new ApiResponse(200, "Doctor Updated", null));
    }

    public ResponseEntity<ApiResponse> update(String login , DoctorDTO doctorDTO) {
        Doctor byLogin = doctorRepository.findByLogin(login);
        if (byLogin == null)
            return ResponseEntity.notFound().build();
        Photo photo = photoService.findByUrl(doctorDTO.getPhotoUrl());
        Doctor entity = doctorMapper.toEntity(doctorDTO);
        entity.setId(byLogin.getId());
        entity.setLogin(byLogin.getLogin());
        entity.setPassword(byLogin.getPassword());
        entity.setChatId(byLogin.getChatId());
        entity.setState(byLogin.getState());
        entity.setPhoto(photo);


        saveToDb(entity);
        return ResponseEntity.ok().body(new ApiResponse(200, "Doctor Updated", doctorMapper.toDTO(entity)));
    }


    public ResponseEntity<ApiResponse> deleteByLogin(String login) {
        if (doctorRepository.existsByLogin(login)) {
            doctorRepository.deleteByLogin(login);
            return ResponseEntity.ok().body(new ApiResponse(200, "Doctor Deleted", null));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<ApiResponse> createLogin(LoginDTO loginDTO) {
        Doctor doctor = new Doctor();
        doctor.setLogin(loginDTO.getLogin());
        doctor.setPassword(loginDTO.getPassword());
        doctorRepository.save(doctor);
        return ResponseEntity.ok().body(new ApiResponse(200, "Login Created", "id:" + doctor.getId()));
    }

    public Optional<Doctor> findByChatId(Long chatId) {
        return doctorRepository.findByChatId(chatId);
    }

    public ResponseEntity<ApiResponse> signIn(LoginDTO loginDTO) {
        Doctor doctor = doctorRepository.findByLogin(loginDTO.getLogin());
        if (doctor != null) {
            if (Objects.equals(doctor.getPassword(), loginDTO.getPassword())) {
                return ResponseEntity.ok().body(new ApiResponse(200, "Success", doctor.getId()));
            }
        }
        return ResponseEntity.notFound().build();
    }

   public ResponseEntity<ApiResponse> updateState(Long chatId, UserState userState) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByChatId(chatId);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setState(userState);
            doctorRepository.save(doctor);
            return ResponseEntity.ok().body(new ApiResponse(200, "Success", "state: " + doctor.getState()));
        }
        return ResponseEntity.notFound().build();
    }

    public void saveChatId(String login, String chatId) {
        doctorRepository.saveChatId(login, chatId);
    }

    public Set<String> parseSpecialities(String input) {
        String[] parts = input.split(",");
        Set<String> specialities = new HashSet<>();
        Arrays.stream(parts).forEach(part -> {
            String trim = part.trim();
            if (!trim.isEmpty()) {
                specialities.add(trim);
            }
        });
        return specialities;
    }
}
