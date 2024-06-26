package med.support.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import med.support.entity.Doctor;
import med.support.entity.Photo;
import med.support.enums.UserState;
import med.support.model.*;
import med.support.model.ApiResponse;
import med.support.repository.DoctorRepository;
import med.support.service.DoctorService;
import med.support.service.PhotoService;
import med.support.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MedicalBot extends TelegramLongPollingBot {

    private final DoctorService doctorService;

    private final DoctorRepository doctorRepository;

    private final PhotoService photoService;

    private final ValidationService validationService;

    private final BotService botService;

    private UserState userState = null;

    private HashMap<Long, String> logins = new HashMap<>();
    private HashMap<Long, DoctorDTO> doctorsList = new HashMap<>();

    private HashMap<Long, UserState> userStateHashMap=new HashMap<>();

    private HashMap<Long, ExperienceDTO> experienceList = new HashMap<>();

    private HashMap<Long, EducationDTO> educationList = new HashMap<>();

    private HashMap<Long, ReceptionAddressDTO> receptionList = new HashMap<>();

    private HashMap<Long, ServiceDTO> servicesList = new HashMap<>();

    private HashMap<Long, ContactDTO> contactList = new HashMap<>();

    private HashMap<Long, String> photoFileIds = new HashMap<>();

    private Set<String> languages = new HashSet<>();
    public static HashMap<Long, Set<String>> userSelections = new HashMap<>();


    @Value("${bot.token}")
    public String token;

    @Value("${bot.username}")
    public String username;

    @Value("${photoUploadDir}")
    public String imgLocation;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @SneakyThrows

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();
            Long chatId = message.getChatId();

            Optional<Doctor> currencyUser = doctorService.findByChatId(chatId);

            if (currencyUser.isPresent()) {
                userState = currencyUser.get().getState();

                switch (userState) {

                    case LOGIN -> {
                        String userPassword = text;
                        String login = doctorService.findByChatId(chatId).get().getLogin();
                        ResponseEntity<ApiResponse> result =
                                doctorService.signIn(LoginDTO.builder().login(login).password(userPassword).build());
                        if (result.getStatusCode().value() == 200) {
                            execute(botService.enterFirstName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.FIRSTNAME);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO == null) {
                                doctorsList.put(chatId, DoctorDTO.builder()
                                        .chatId(chatId).login(login).password(userPassword).build());
                            } else {
                                doctorDTO.setLogin(login);
                                doctorDTO.setPassword(userPassword);
                                doctorsList.put(chatId, doctorDTO);
                            }
                            userStateHashMap.put(chatId,UserState.FIRSTNAME);
                            return;
                        }
                        userStateHashMap.put(chatId,UserState.LOGIN);
                    }

                    case FIRSTNAME -> {
                        String validationMessage = validationService.validateNames(text, "Ism");
                        if (validationMessage == null) {
                            String firstName = text;
                            execute(botService.enterLastName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.LASTNAME);
                            userStateHashMap.put(chatId,UserState.LASTNAME);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setFirstname(firstName);
                            doctorsList.put(chatId, doctorDTO);
                        } else {
                            execute(botService.validationMessage(chatId.toString(), validationMessage));
                        }
                    }

                    case LASTNAME -> {
                        String validationMessage = validationService.validateNames(text, "Familya");
                        if (validationMessage == null) {
                            String lastName = text;
                            execute(botService.enterSurname(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SURNAME);
                            userStateHashMap.put(chatId,UserState.SURNAME);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setLastname(lastName);
                            doctorsList.put(chatId, doctorDTO);
                        } else {
                            execute(botService.validationMessage(chatId.toString(), validationMessage));
                        }
                    }

                    case SURNAME -> {
                        String validationMessage = validationService.validateNames(text, "Surname");
                        if (validationMessage == null) {
                            String surname = text;
                            execute(botService.enterPhone(chatId.toString()));
                            doctorService.updateState(chatId, UserState.PHONE);
                            userStateHashMap.put(chatId,UserState.PHONE);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setSurname(surname);
                            doctorsList.put(chatId, doctorDTO);
                        } else {
                            execute(botService.validationMessage(chatId.toString(), validationMessage));
                        }
                    }

                    case PHONE -> {
                        String validationMessage = validationService.validatePhoneNumber(text);
                        if (validationMessage == null) {
                            String phone = text;
                            execute(botService.enterOutLine(chatId.toString()));
                            doctorService.updateState(chatId, UserState.OUTLINE);
                            userStateHashMap.put(chatId,UserState.OUTLINE);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setPhone(phone);
                            doctorsList.put(chatId, doctorDTO);
                        } else {
                            execute(botService.validationMessage(chatId.toString(), validationMessage));
                        }
                    }

                    case OUTLINE -> {
                        String outLine = text;
                        execute(botService.enterMotto(chatId.toString()));
                        doctorService.updateState(chatId, UserState.MOTTO);
                        userStateHashMap.put(chatId,UserState.MOTTO);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setOutline(outLine);
                        doctorsList.put(chatId, doctorDTO);
                    }

                    case MOTTO -> {
                        String motto = text;
                        execute(botService.sendPhoto(chatId.toString()));
                        doctorService.updateState(chatId, UserState.PHOTO);
                        userStateHashMap.put(chatId,UserState.PHOTO);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setMotto(motto);
                        doctorsList.put(chatId, doctorDTO);
                    }

                    case PHOTO -> {
                        if (message.hasPhoto()) {
                            String fileId = message.getPhoto().stream()
                                    .max(Comparator.comparing(PhotoSize::getFileSize))
                                    .orElseThrow(() -> new IllegalStateException("Photo is missing"))
                                    .getFileId();
                            GetFile getFileMethod = new GetFile(fileId);
                            File file = execute(getFileMethod);
                            String fileId1 = file.getFileId();
                            photoFileIds.put(chatId, fileId1);
                            String filePath = file.getFilePath();
                            String fileName = UUID.randomUUID() + Paths.get(filePath).getFileName().toString();
                            String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath;
                            URL url = new URL(fileUrl);
                            InputStream input = url.openStream();
                            String targetPath = imgLocation  + fileName;
                            Files.copy(input, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                            input.close();
                            DoctorDTO doctorDTO = doctorsList.get(chatId);

                            Photo photo = photoService.savePhotoFromTelegram(targetPath, doctorDTO.getLogin());
                            Doctor doctor = doctorRepository.findByLogin(doctorDTO.getLogin());
                            doctor.setPhoto(photo);
                            doctorService.saveToDb(doctor);

                            String httpUrl = photo.getHttpUrl();
                            doctorDTO.setPhotoUrl(httpUrl);
                            doctorsList.put(chatId, doctorDTO);
//                            System.out.println(doctorDTO);
                            execute(botService.enterSpeciality(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SPECIALITY);
                            userStateHashMap.put(chatId,UserState.SPECIALITY);
                            return;
                        }
                        execute(botService.sendPhoto(chatId.toString()));

                    }

                    case SPECIALITY -> {
                        String speciality = text;
                        if (!validationService.isValidSpeciality(speciality)) {
                            execute(botService.enterInvalidSpecialityMessage(chatId.toString()));
                            return;
                        }
                        execute(botService.enterLanguage(chatId.toString()));
                        doctorService.updateState(chatId, UserState.LANGUAGE);
                        userStateHashMap.put(chatId,UserState.LANGUAGE);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        if (doctorDTO == null) {
                            doctorDTO = new DoctorDTO();
                        }
                        doctorDTO.setSpecialty(doctorService.parseSpecialities(speciality));
//                        System.out.println(doctorDTO);
                        doctorsList.put(chatId, doctorDTO);
                    }


                    case EXPERIENCE -> {
                        userStateHashMap.put(chatId,UserState.EXPERIENCE);
                    }

                    case EXPERIENCE_WORKPLACE -> {
                        String workPlace = text;
                        if (!validationService.validateWorkplace(workPlace)) {
                            execute(botService.sendInvalidWorkplaceMessage(chatId.toString()));
                            return;
                        }
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().workplace(workPlace).build());
                            experienceDTO=experienceList.get(chatId);
                        } else {
                            experienceDTO.setWorkplace(workPlace);
                            experienceList.put(chatId, experienceDTO);
                        }
                        userStateHashMap.put(chatId,UserState.EXPERIENCE_BEGIN_DATE);
                        execute(botService.enterExperience(chatId.toString()));
                        if (experienceDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                            doctorService.updateState(chatId, UserState.EXPERIENCE_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getExperience() == null) {
                                doctorDTO.setExperience(new HashSet<>());
                            }
                            doctorDTO.getExperience().add(experienceDTO);
                            doctorsList.put(chatId, doctorDTO);
                            experienceList.remove(chatId);
                            execute(botService.isSaveExperience(chatId.toString()));
                            return;
                        }
                    }

                    case EXPERIENCE_BEGIN_DATE -> {
                        String beginDate = text;
                        String validationResponse = validationService.validateBeginDate(beginDate, "dd/MM/yyyy");  // Sana formatini bu yerda ko'rsating

                        if (validationResponse != null) {
                            execute(botService.sendBedinDateMessage(chatId.toString(), validationResponse));  // Agar validatsiya xato bo'lsa, xato xabarini jo'natish
                        } else {
                            ExperienceDTO experienceDTO = experienceList.get(chatId);
                            if (experienceDTO == null) {
                                experienceList.put(chatId, ExperienceDTO.builder().beginDate(beginDate).build());
                                experienceDTO=experienceList.get(chatId);
                            } else {
                                experienceDTO.setBeginDate(beginDate);
                                experienceList.put(chatId, experienceDTO);
                            }
                            userStateHashMap.put(chatId,UserState.EXPERIENCE_END_DATE);
                            execute(botService.enterExperience(chatId.toString()));  // Keyingi bosqichga o'tish
                            if (experienceDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                                doctorService.updateState(chatId, UserState.EXPERIENCE_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getExperience() == null) {
                                    doctorDTO.setExperience(new HashSet<>());
                                }
                                doctorDTO.getExperience().add(experienceDTO);
                                doctorsList.put(chatId, doctorDTO);
                                experienceList.remove(chatId);
                                execute(botService.isSaveExperience(chatId.toString()));
                                return;
                            }
                        }
                    }


                    case EXPERIENCE_END_DATE -> {
                        String endDate = text;
                        String validationResponse = validationService.validateBeginDate(endDate, "dd/MM/yyyy");  // Sana formatini bu yerda ko'rsating
                        if (validationResponse != null) {
                            execute(botService.sendBedinDateMessage(chatId.toString(), validationResponse));  // Agar validatsiya xato bo'lsa, xato xabarini jo'natish
                        } else {
                            ExperienceDTO experienceDTO = experienceList.get(chatId);
                            if (experienceDTO == null) {
                                experienceList.put(chatId, ExperienceDTO.builder().endDate(endDate).build());
                                experienceDTO=experienceList.get(chatId);
                            } else {
                                experienceDTO.setEndDate(endDate);
                                experienceList.put(chatId, experienceDTO);
                            }
                            userStateHashMap.put(chatId,UserState.EXPERIENCE_POSITION);
                            execute(botService.enterExperience(chatId.toString()));
                            if (experienceDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                                doctorService.updateState(chatId, UserState.EXPERIENCE_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getExperience() == null) {
                                    doctorDTO.setExperience(new HashSet<>());
                                }
                                doctorDTO.getExperience().add(experienceDTO);
                                doctorsList.put(chatId, doctorDTO);
                                experienceList.remove(chatId);
                                execute(botService.isSaveExperience(chatId.toString()));
                                return;
                            }
                        }
                    }

                    case EXPERIENCE_POSITION -> {
                        String position = text;
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().position(position).build());
                            experienceDTO=experienceList.get(chatId);
                        } else {
                            experienceDTO.setPosition(position);
                            experienceList.put(chatId, experienceDTO);
                        }
                        if (experienceDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                            doctorService.updateState(chatId, UserState.EXPERIENCE_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getExperience() == null) {
                                doctorDTO.setExperience(new HashSet<>());
                            }
                            doctorDTO.getExperience().add(experienceDTO);
                            doctorsList.put(chatId, doctorDTO);
                            experienceList.remove(chatId);
                            execute(botService.isSaveExperience(chatId.toString()));
                            return;
                        }
                        execute(botService.enterExperience(chatId.toString()));
                    }

                    case ACHIEVEMENT -> {
                        String achievement = text;
                        if (!validationService.isValidAchievement(achievement)) {
                            execute(botService.enterInvalidAchievementMessage(chatId.toString()));
                            return;
                        }
                        execute(botService.enterEducation(chatId.toString()));
                        doctorService.updateState(chatId, UserState.EDUCATION);
                        userStateHashMap.put(chatId,UserState.EDUCATION);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setAchievement(doctorService.parseSpecialities(achievement));
                        doctorsList.put(chatId, doctorDTO);
                    }

                    case EDUCATION -> {
                        userStateHashMap.put(chatId,UserState.EDUCATION);
                    }

                    case EDUCATION_NAME -> {
                        String name = text;
                        if (!validationService.validateWorkplace(name)) {
                            execute(botService.sendInvalidWorkplaceMessage(chatId.toString()));
                            return;
                        }
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().name(name).build());
                            educationDTO=educationList.get(chatId);
                        } else {
                            educationDTO.setName(name);
                            educationList.put(chatId, educationDTO);
                        }
                        userStateHashMap.put(chatId,UserState.EDUCATION_START_YEAR);
                        execute(botService.enterEducation(chatId.toString()));
                        if (educationDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                            doctorService.updateState(chatId, UserState.EDUCATION_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getEducation() == null) {
                                doctorDTO.setEducation(new HashSet<>());
                            }
                            doctorDTO.getEducation().add(educationDTO);
                            doctorsList.put(chatId, doctorDTO);
                            educationList.remove(chatId);
                            execute(botService.isSaveEducation(chatId.toString()));
                            return;
                        }
                    }

                    case EDUCATION_START_YEAR -> {
                        String startYear = text;
                        if (validationService.isValidYear(startYear)) {
                            EducationDTO educationDTO = educationList.get(chatId);
                            if (educationDTO == null) {
                                educationList.put(chatId, EducationDTO.builder().startYear(Integer.valueOf(startYear)).build());
                            } else {
                                educationDTO.setStartYear(Integer.valueOf(startYear));
                                educationList.put(chatId, educationDTO);
                            }
                            userStateHashMap.put(chatId,UserState.EDUCATION_END_YEAR);
                            execute(botService.enterEducation(chatId.toString()));
                            if (educationDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                                doctorService.updateState(chatId, UserState.EDUCATION_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getEducation() == null) {
                                    doctorDTO.setEducation(new HashSet<>());
                                }
                                doctorDTO.getEducation().add(educationDTO);
                                doctorsList.put(chatId, doctorDTO);
                                educationList.remove(chatId);
                                execute(botService.isSaveEducation(chatId.toString()));
                                return;
                            }
                        } else {
                            execute(botService.enterTrueYear(chatId.toString()));
                        }
                    }

                    case EDUCATION_END_YEAR -> {
                        String endYear = text;
                        if (validationService.isValidYear(endYear)) {
                            EducationDTO educationDTO = educationList.get(chatId);
                            if (educationDTO == null) {
                                educationList.put(chatId, EducationDTO.builder().endYear(Integer.valueOf(endYear)).build());
                            } else {
                                educationDTO.setEndYear(Integer.valueOf(endYear));
                                educationList.put(chatId, educationDTO);
                            }
                            userStateHashMap.put(chatId,UserState.EDUCATION_FACULTY);
                            execute(botService.enterEducation(chatId.toString()));
                            if (educationDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                                doctorService.updateState(chatId, UserState.EDUCATION_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getEducation() == null) {
                                    doctorDTO.setEducation(new HashSet<>());
                                }
                                doctorDTO.getEducation().add(educationDTO);
                                doctorsList.put(chatId, doctorDTO);
                                educationList.remove(chatId);
                                execute(botService.isSaveEducation(chatId.toString()));
                                return;
                            }
                        } else {
                            execute(botService.enterTrueYear(chatId.toString()));
                        }
                    }


                    case EDUCATION_FACULTY -> {
                        String faculty = text;
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().faculty(faculty).build());
                            educationDTO=educationList.get(chatId);
                        } else {
                            educationDTO.setFaculty(faculty);
                            educationList.put(chatId, educationDTO);
                        }
                        if (educationDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.EDUCATION_FINISHED);
                            doctorService.updateState(chatId, UserState.EDUCATION_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getEducation() == null) {
                                doctorDTO.setEducation(new HashSet<>());
                            }
                            doctorDTO.getEducation().add(educationDTO);
                            doctorsList.put(chatId, doctorDTO);
                            educationList.remove(chatId);
                            execute(botService.isSaveEducation(chatId.toString()));
                            return;
                        }
                        execute(botService.enterEducation(chatId.toString()));
                    }

                    case RECEPTION_ADDRESS -> {
                        userStateHashMap.put(chatId,UserState.RECEPTION_ADDRESS);
                    }

                    case ADDRESS_NAME -> {
                        String addressName = text;
                        if (!validationService.validateWorkplace(addressName)) {
                            execute(botService.sendInvalidWorkplaceMessage(chatId.toString()));
                            return;
                        }
                        ReceptionAddressDTO addressDTO = receptionList.get(chatId);
                        if (addressDTO == null) {
                            receptionList.put(chatId, ReceptionAddressDTO.builder().addressName(addressName).build());
                            addressDTO=receptionList.get(chatId);
                        } else {
                            addressDTO.setAddressName(addressName);
                            receptionList.put(chatId, addressDTO);
                        }
                        userStateHashMap.put(chatId,UserState.ADDRESS_URL);
                        execute(botService.enterReceptionAddress(chatId.toString()));
                        if (addressDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.ADDRESS_FINISHED);
                            doctorService.updateState(chatId, UserState.ADDRESS_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getReceptionAddress() == null) {
                                doctorDTO.setReceptionAddress(new HashSet<>());
                            }
                            doctorDTO.getReceptionAddress().add(addressDTO);
                            doctorsList.put(chatId, doctorDTO);
                            receptionList.remove(chatId);
                            execute(botService.isSaveReceptionAddress(chatId.toString()));
                            return;
                        }
                    }

                    case ADDRESS_URL -> {
                        if (message.hasLocation()) {
                            Location location = message.getLocation();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String addressUrl = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                            ReceptionAddressDTO addressDTO = receptionList.get(chatId);
                            if (addressDTO == null) {
                                receptionList.put(chatId, ReceptionAddressDTO.builder().httpUrl(addressUrl).build());
                                addressDTO=receptionList.get(chatId);
                            } else {
                                addressDTO.setHttpUrl(addressUrl);
                                receptionList.put(chatId, addressDTO);
                            }
                            if (addressDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.ADDRESS_FINISHED);
                                doctorService.updateState(chatId, UserState.ADDRESS_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getReceptionAddress() == null) {
                                    doctorDTO.setReceptionAddress(new HashSet<>());
                                }
                                doctorDTO.getReceptionAddress().add(addressDTO);
                                doctorsList.put(chatId, doctorDTO);
                                receptionList.remove(chatId);
                                execute(botService.isSaveReceptionAddress(chatId.toString()));
                                return;
                            }
                            execute(botService.enterReceptionAddress(chatId.toString()));
                        } else {
                            execute(botService.enterAddressUrl(chatId.toString()));
                        }

                    }

                    case SERVICE -> {
                        userStateHashMap.put(chatId,UserState.SERVICE);
                    }

                    case SERVICE_NAME -> {
                        String serviceName = text;
                        if (!validationService.validateWorkplace(serviceName)) {
                            execute(botService.sendInvalidWorkplaceMessage(chatId.toString()));
                            return;
                        }
                        ServiceDTO serviceDTO = servicesList.get(chatId);
                        if (serviceDTO == null) {
                            servicesList.put(chatId, ServiceDTO.builder().name(serviceName).build());
                            serviceDTO=servicesList.get(chatId);
                        } else {
                            serviceDTO.setName(serviceName);
                            servicesList.put(chatId, serviceDTO);
                        }
                        userStateHashMap.put(chatId,UserState.SERVICE_PRICE);
                        execute(botService.enterServices(chatId.toString()));
                        if (serviceDTO.isComplete()){
                            userStateHashMap.put(chatId,UserState.SERVICE_FINISHED);
                            doctorService.updateState(chatId, UserState.SERVICE_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getService() == null) {
                                doctorDTO.setService(new HashSet<>());
                            }
                            doctorDTO.getService().add(serviceDTO);
                            doctorsList.put(chatId, doctorDTO);
                            servicesList.remove(chatId);
                            execute(botService.isSaveService(chatId.toString()));
                            return;
                        }
                    }

                    case SERVICE_PRICE -> {
                        String servicePrice = text;
                        if (validationService.isValidInteger(servicePrice)) {
                            ServiceDTO serviceDTO = servicesList.get(chatId);
                            if (serviceDTO == null) {
                                servicesList.put(chatId, ServiceDTO.builder().price(Integer.valueOf(servicePrice)).build());
                                serviceDTO=servicesList.get(chatId);
                            } else {
                                serviceDTO.setPrice(Integer.valueOf(servicePrice));
                                servicesList.put(chatId, serviceDTO);
                            }
                            if (serviceDTO.isComplete()){
                                userStateHashMap.put(chatId,UserState.SERVICE_FINISHED);
                                doctorService.updateState(chatId, UserState.SERVICE_FINISHED);
                                DoctorDTO doctorDTO = doctorsList.get(chatId);
                                if (doctorDTO.getService() == null) {
                                    doctorDTO.setService(new HashSet<>());
                                }
                                doctorDTO.getService().add(serviceDTO);
                                doctorsList.put(chatId, doctorDTO);
                                servicesList.remove(chatId);
                                execute(botService.isSaveService(chatId.toString()));
                                return;
                            }
                            execute(botService.enterServices(chatId.toString()));
                        } else {
                            execute(botService.enterTruePrice(chatId.toString()));
                        }

                    }

                    case CONTACT -> {
                        String contactValue = text;
                        if (validationService.isValidContact(contactValue)) {
                            ContactDTO contactDTO = contactList.get(chatId);
                            contactDTO.setValue(contactValue);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            if (doctorDTO.getContact() == null) {
                                doctorDTO.setContact(new HashSet<>());
                            }
                            doctorDTO.getContact().add(contactDTO);
                            doctorsList.put(chatId, doctorDTO);
                            contactList.remove(chatId);
                            userStateHashMap.put(chatId,UserState.CONTACT_FINISHED);
                            doctorService.updateState(chatId, UserState.CONTACT_FINISHED);
                            execute(botService.saveContact(chatId.toString()));
                        } else {
                            execute(botService.enterTrueContact(chatId.toString()));
                        }

                    }

                }
            }

            if (Objects.equals(text, "/start")) {
                userStateHashMap.put(chatId, UserState.START);
            } else if (userStateHashMap.get(chatId) == null || userStateHashMap.get(chatId) == UserState.DEFAULT) {
                userStateHashMap.put(chatId,UserState.DEFAULT);
                execute(botService.sendStart(chatId.toString()));
            }

            switch (userStateHashMap.get(chatId)) {

                case START -> {
                    if (!text.equals("/start")) {
                        String login = text;
                        if (doctorService.findByLogin(login).getStatusCode().value() == 200) {
                            DoctorDTO doctorDTO = (DoctorDTO) doctorService.findByLogin(login).getBody().getData();
                            if (doctorDTO.getContact().isEmpty()) {
                                doctorService.saveChatId(login, chatId.toString());
                            }
                            doctorService.updateState(chatId, UserState.LOGIN);
                            userStateHashMap.put(chatId,UserState.LOGIN);
                            logins.put(chatId, login);
                            execute(botService.enterPassword(chatId.toString()));
                            return;
                        }
                        execute(botService.enterAgainLogin(chatId.toString()));
                        return;
                    }
                    execute(botService.enterLogin(chatId.toString()));
                }
                case LOGIN -> {
                    String userPassword = text;
                    String login = logins.get(chatId);
                    ResponseEntity<ApiResponse> result =
                            doctorService.signIn(LoginDTO.builder().login(login).password(userPassword).build());
//                    System.out.println(result);
                    if (result.getStatusCode().value() == 200) {
                        execute(botService.alreadyExistsMessage(chatId.toString()));
                        return;
                    }
                    execute(botService.enterAgainPassword(chatId.toString()));
                    userStateHashMap.put(chatId,UserState.LOGIN);
                }

                case EXPERIENCE_POSITION -> {
                    doctorService.updateState(chatId, UserState.EXPERIENCE_POSITION);
                }

                case EDUCATION_FACULTY -> {
                    doctorService.updateState(chatId, UserState.EDUCATION_FACULTY);
                }

                case ADDRESS_URL -> {
                    doctorService.updateState(chatId, UserState.ADDRESS_URL);
                }

                case SERVICE_PRICE -> {
                    doctorService.updateState(chatId, UserState.SERVICE_PRICE);
                }

            }
        }
        else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
//            System.out.println(data);

            if (doctorService.findByChatId(chatId).orElse(null) == null) {
                switch (userStateHashMap.get(chatId)) {
                    case LOGIN -> {
                        switch (data) {
                            case "yes" -> {
                                doctorService.saveChatId(logins.get(chatId), chatId.toString());
                                doctorService.updateState(chatId, UserState.LOGIN);
                                userStateHashMap.put(chatId,UserState.LOGIN);
                                execute(botService.enterPassword(chatId.toString()));
                            }
                            case "no" -> {
                                execute(botService.sendMessageForNo(chatId.toString()));
                                userStateHashMap.put(chatId,UserState.ALREADY_REGISTRATED);
                            }
                        }
                    }
                    case ALREADY_REGISTRATED -> {
                        switch (data) {
                            case "new" -> {
                                doctorService.saveChatId(logins.get(chatId), chatId.toString());
                                execute(botService.sendMessageForNew(chatId.toString()));
                                logins.remove(chatId);
                            }
                            case "old" -> {
                                execute(botService.sendMessageForOld(chatId.toString()));
                                logins.remove(chatId);
                            }
                        }
                    }
                }
                return;

            }

            switch (doctorService.findByChatId(chatId).get().getState()) {

                case LANGUAGE -> {

                    switch (data) {

                        case "uzb", "rus", "eng" -> {
                            languages.add(data);
                        }
                        case "confirm" -> {
                            String lan = "";
                            for (String language : languages) {
                                lan += language + ",";
                            }
                            execute(botService.sendSelectLanguageMessage(chatId.toString(), lan));
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setLanguage(doctorService.parseSpecialities(lan));
//                            System.out.println(doctorDTO);
                            languages.clear();
                        }
                        case "cansel" -> {
                            execute(botService.enterLanguage(chatId.toString()));
                        }
                        case "save" -> {
                            execute(botService.enterExperience(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE);
                            userStateHashMap.put(chatId,UserState.EXPERIENCE);
                        }

                    }
                }

                case EXPERIENCE, EXPERIENCE_WORKPLACE, EXPERIENCE_BEGIN_DATE, EXPERIENCE_END_DATE, EXPERIENCE_POSITION -> {
                    userSelections.putIfAbsent(chatId, new HashSet<>());
                    userSelections.get(chatId).add(data);

                    switch (data) {

                        case "workplace" -> {
                            execute(botService.enterWorkPlace(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE_WORKPLACE);
                        }

                        case "beginDate" -> {
                            execute(botService.enterBeginDate(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE_BEGIN_DATE);
                        }

                        case "endDate" -> {
                            execute(botService.enterEndDate(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE_END_DATE);
                        }

                        case "position" -> {
                            execute(botService.enterPosition(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE_POSITION);
//                            System.out.println("keldi");
                        }

                    }

                }

                case EXPERIENCE_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterAchievement(chatId.toString()));
                            doctorService.updateState(chatId, UserState.ACHIEVEMENT);
                            userStateHashMap.put(chatId,UserState.ACHIEVEMENT);
                            experienceList.remove(chatId);
                        }

                        case "add" -> {
                            userSelections.remove(chatId);
                            execute(botService.enterExperience(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE);
                            userStateHashMap.put(chatId,UserState.EXPERIENCE);
                        }

                    }
                }

                case EDUCATION, EDUCATION_NAME, EDUCATION_START_YEAR, EDUCATION_END_YEAR, EDUCATION_FACULTY -> {
                    userSelections.putIfAbsent(chatId, new HashSet<>());
                    userSelections.get(chatId).add(data);

                    switch (data) {

                        case "name" -> {
                            execute(botService.enterEducationName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION_NAME);
                        }

                        case "startYear" -> {
                            execute(botService.enterEducationStartYear(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION_START_YEAR);
                        }

                        case "endYear" -> {
                            execute(botService.enterEducationEndYear(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION_END_YEAR);
                        }

                        case "faculty" -> {
                            execute(botService.enterEducationFaculty(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION_FACULTY);
//                            System.out.println("keldi");
                        }
                    }

                }

                case EDUCATION_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterReceptionAddress(chatId.toString()));
                            doctorService.updateState(chatId, UserState.RECEPTION_ADDRESS);
                            userStateHashMap.put(chatId,UserState.RECEPTION_ADDRESS);
                            educationList.remove(chatId);
                        }

                        case "add" -> {
                            userSelections.remove(chatId);
                            execute(botService.enterEducation(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION);
                            userStateHashMap.put(chatId,UserState.EDUCATION);
                        }

                    }
                }

                case RECEPTION_ADDRESS, ADDRESS_NAME, ADDRESS_URL -> {
                    userSelections.putIfAbsent(chatId, new HashSet<>());
                    userSelections.get(chatId).add(data);

                    switch (data) {

                        case "addressName" -> {
                            execute(botService.enterAddressName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.ADDRESS_NAME);
                        }

                        case "addressUrl" -> {
                            execute(botService.enterAddressUrl(chatId.toString()));
                            doctorService.updateState(chatId, UserState.ADDRESS_URL);
                        }

                    }

                }

                case ADDRESS_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterServices(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SERVICE);
                            userStateHashMap.put(chatId,UserState.SERVICE);
                            receptionList.remove(chatId);
                        }

                        case "add" -> {
                            userSelections.remove(chatId);
                            execute(botService.enterReceptionAddress(chatId.toString()));
                            doctorService.updateState(chatId, UserState.RECEPTION_ADDRESS);
                            userStateHashMap.put(chatId,UserState.RECEPTION_ADDRESS);
                        }

                    }
                }

                case SERVICE, SERVICE_NAME, SERVICE_PRICE -> {
                    userSelections.putIfAbsent(chatId, new HashSet<>());
                    userSelections.get(chatId).add(data);

                    switch (data) {

                        case "serviceName" -> {
                            execute(botService.enterServiceName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SERVICE_NAME);
                        }

                        case "servicePrice" -> {
                            execute(botService.enterServicePrice(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SERVICE_PRICE);
                        }

                    }
                }

                case SERVICE_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterContact(chatId.toString()));
                            doctorService.updateState(chatId, UserState.CONTACT);
                            userStateHashMap.put(chatId,UserState.CONTACT);
                            servicesList.remove(chatId);
                        }

                        case "add" -> {
                            userSelections.remove(chatId);
                            execute(botService.enterServices(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SERVICE);
                            userStateHashMap.put(chatId,UserState.SERVICE);
                        }

                    }
                }

                case CONTACT -> {
                    if (data.equals("telegram") || data.equals("youTube") || data.equals("mail")) {
                        ContactDTO contactDTO = contactList.get(chatId);
                        if (contactDTO == null) {
                            contactList.put(chatId, ContactDTO.builder().contactType(data).build());
                            contactDTO=contactList.get(chatId);
                        } else {
                            contactDTO.setContactType(data);
                            contactList.put(chatId, contactDTO);
                        }
                        execute(botService.enterContactValue(chatId.toString()));
                    } else {
                        execute(botService.enterContact(chatId.toString()));
                    }
                }

                case CONTACT_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.finished(chatId.toString()));
                            doctorService.updateState(chatId, UserState.REGISTRATION_FINISHED);
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setUserState(UserState.REGISTRATION_FINISHED);
                            doctorService.update(doctorDTO.getLogin(), chatId, UserState.REGISTRATION_FINISHED, doctorDTO);
                            execute(botService.sendAllInformation(chatId.toString(), doctorDTO, photoFileIds.get(chatId)));
                            doctorsList.remove(chatId);
                            userStateHashMap.remove(chatId);
                        }

                        case "add" -> {
                            execute(botService.enterContact(chatId.toString()));
                            doctorService.updateState(chatId, UserState.CONTACT);
                            userStateHashMap.put(chatId,UserState.CONTACT);
                        }

                    }
                }

            }
        }
    }

}
