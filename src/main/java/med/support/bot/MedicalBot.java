package med.support.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import med.support.entity.Doctor;
import med.support.enums.UserState;
import med.support.model.*;
import med.support.service.DoctorService;
import med.support.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MedicalBot extends TelegramLongPollingBot {

    private final DoctorService doctorService;

    private final ValidationService validationService;

    private final BotService botService;

    private UserState userState = null;

    private HashMap<Long, DoctorDTO> doctorsList = new HashMap<>();

//    private void start() {
//        DoctorDTO put = doctorsList.put(5269233777L, new DoctorDTO());
//    }

    private HashMap<Long, ExperienceDTO> experienceList = new HashMap<>();

    private HashMap<Long, EducationDTO> educationList = new HashMap<>();

    private Set<String> languages = new HashSet<>();
    public static HashMap<Long, Set<String>> userSelections = new HashMap<>();


    @Value("${bot.token}")
    public String token;

    @Value("${bot.username}")
    public String username;

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
//        start();
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
                        System.out.println(result);
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
                            userState = UserState.FIRSTNAME;
                            return;
                        }
                        execute(botService.enterAgainPassword(chatId.toString()));
                        userState = UserState.LOGIN;
                    }

                    case FIRSTNAME -> {
                        String validationMessage = validationService.validateNames(text, "Ism");
                        if (validationMessage == null) {
                            String firstName = text;
                            execute(botService.enterLastName(chatId.toString()));
                            doctorService.updateState(chatId, UserState.LASTNAME);
                            userState = UserState.LASTNAME;
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
                            userState = UserState.SURNAME;
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
                            userState = UserState.PHONE;
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
                            userState = UserState.OUTLINE;
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
                        userState = UserState.MOTTO;
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setOutline(outLine);
                        doctorsList.put(chatId, doctorDTO);
                    }

                    case MOTTO -> {
                        String motto = text;
                        execute(botService.sendPhoto(chatId.toString()));
                        doctorService.updateState(chatId, UserState.PHOTO);
                        userState = UserState.PHOTO;
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setMotto(motto);
                        doctorsList.put(chatId, doctorDTO);
                    }

                    case PHOTO -> {
                        if (message.hasPhoto()) {
                            execute(botService.enterSpeciality(chatId.toString()));
                            doctorService.updateState(chatId, UserState.SPECIALITY);
                            userState = UserState.SPECIALITY;
                            DoctorDTO doctorDTO = doctorsList.get(chatId);
                            doctorDTO.setPhotoUrl("url");
                            doctorsList.put(chatId, doctorDTO);
                            return;
                        }
                        execute(botService.sendPhoto(chatId.toString()));
                    }
                    case SPECIALITY -> {
                        String speciality = text;
                        execute(botService.enterLanguage(chatId.toString()));
                        doctorService.updateState(chatId, UserState.LANGUAGE);
                        userState = UserState.LANGUAGE;
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setSpecialty(doctorService.parseSpecialities(speciality));
                        System.out.println(doctorDTO);
                        doctorsList.put(chatId, doctorDTO);
                    }
                    case EXPERIENCE -> {
//                        execute(botService.enterExperience(chatId.toString()));
//
                        userState = UserState.EXPERIENCE;
                    }
                    case EXPERIENCE_WORKPLACE -> {
                        String workPlace = text;
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().workplace(workPlace).build());
                        } else {
                            experienceDTO.setWorkplace(workPlace);
                            experienceList.put(chatId, experienceDTO);
                        }
                        userState = UserState.EXPERIENCE_BEGIN_DATE;
                        execute(botService.enterExperience(chatId.toString()));
                    }
                    case EXPERIENCE_BEGIN_DATE -> {
                        String beginDate = text;
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().beginDate(beginDate).build());
                        } else {
                            experienceDTO.setBeginDate(beginDate);
                            experienceList.put(chatId, experienceDTO);
                        }
                        userState = UserState.EXPERIENCE_END_DATE;
                        execute(botService.enterExperience(chatId.toString()));
                    }
                    case EXPERIENCE_END_DATE -> {
                        String endDate = text;
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().endDate(endDate).build());
                        } else {
                            experienceDTO.setEndDate(endDate);
                            experienceList.put(chatId, experienceDTO);
                        }
                        userState = UserState.EXPERIENCE_POSITION;
                        execute(botService.enterExperience(chatId.toString()));
                        System.out.println(experienceDTO);
                    }
                    case EXPERIENCE_POSITION -> {
                        String position = text;
                        ExperienceDTO experienceDTO = experienceList.get(chatId);
                        if (experienceDTO == null) {
                            experienceList.put(chatId, ExperienceDTO.builder().position(position).build());
                        } else {
                            experienceDTO.setPosition(position);
                            experienceList.put(chatId, experienceDTO);
                        }
                        userState = UserState.EXPERIENCE_FINISHED;
                        doctorService.updateState(chatId, UserState.EXPERIENCE_FINISHED);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        if (doctorDTO.getExperience() == null) {
                            doctorDTO.setExperience(new HashSet<>());
                        }
                        doctorDTO.getExperience().add(experienceDTO);
                        doctorsList.put(chatId,doctorDTO);
                        execute(botService.isSaveExperience(chatId.toString()));
                    }
                    case ACHIEVEMENT -> {
                        String achievement = text;
                        execute(botService.enterEducation(chatId.toString()));
                        doctorService.updateState(chatId, UserState.EDUCATION);
                        userState = UserState.EDUCATION;
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        doctorDTO.setAchievement(doctorService.parseSpecialities(achievement));
                        doctorsList.put(chatId, doctorDTO);
                    }
                    case EDUCATION -> {
                        userState = UserState.EDUCATION;
                    }

                    case EDUCATION_NAME -> {
                        String name = text;
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().name(name).build());
                        } else {
                            educationDTO.setName(name);
                            educationList.put(chatId, educationDTO);
                        }
                        userState = UserState.EDUCATION_START_YEAR;
                        execute(botService.enterEducation(chatId.toString()));
                    }
                    case EDUCATION_START_YEAR -> {
                        String startYear = text;
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().startYear(Integer.valueOf(startYear)).build());
                        } else {
                            educationDTO.setStartYear(Integer.valueOf(startYear));
                            educationList.put(chatId, educationDTO);
                        }
                        userState = UserState.EDUCATION_END_YEAR;
                        execute(botService.enterEducation(chatId.toString()));
                    }
                    case EDUCATION_END_YEAR -> {
                        String endYear = text;
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().endYear(Integer.valueOf(endYear)).build());
                        } else {
                            educationDTO.setEndYear(Integer.valueOf(endYear));
                            educationList.put(chatId, educationDTO);
                        }
                        userState = UserState.EDUCATION_FACULTY;
                        execute(botService.enterEducation(chatId.toString()));
                    }
                    case EDUCATION_FACULTY -> {
                        String faculty=text;
                        EducationDTO educationDTO = educationList.get(chatId);
                        if (educationDTO == null) {
                            educationList.put(chatId, EducationDTO.builder().faculty(faculty).build());
                        } else {
                            educationDTO.setFaculty(faculty);
                            educationList.put(chatId, educationDTO);
                        }
                        userState = UserState.EDUCATION_FINISHED;
                        doctorService.updateState(chatId,UserState.EDUCATION_FINISHED);
                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        if (doctorDTO.getEducation()==null){
                            doctorDTO.setEducation(new HashSet<>());
                        }
                        doctorDTO.getEducation().add(educationDTO);
                        doctorsList.put(chatId,doctorDTO);
                        execute(botService.isSaveEducation(chatId.toString()));
                    }
                    case RECEPTION_ADDRESS -> {

                        DoctorDTO doctorDTO = doctorsList.get(chatId);
                        System.out.println(doctorDTO);
                    }
                }
            }

            if (text.equals("/start")) {
                userState = UserState.START;
            } else if (userState == null) {
                execute(botService.sendStart(chatId.toString()));
            }

            switch (userState) {
                case START -> {
                    if (!text.equals("/start")) {
                        String login = text;
                        if (doctorService.findByLogin(login).getStatusCode().value() == 200) {
                            doctorService.saveChatId(login, chatId.toString());
                            doctorService.updateState(chatId, UserState.LOGIN);
                            userState = UserState.LOGIN;
                            execute(botService.enterPassword(chatId.toString()));
                            return;
                        }
                        execute(botService.enterAgainLogin(chatId.toString()));
                        return;
                    }
                    execute(botService.enterLogin(chatId.toString()));
                }
                case EXPERIENCE_POSITION -> {
                    doctorService.updateState(chatId, UserState.EXPERIENCE_POSITION);
                }
                case EDUCATION_FACULTY -> {
                    doctorService.updateState(chatId, UserState.EDUCATION_FACULTY);
                }
            }
        }

        else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            System.out.println(data);
            switch (userState) {

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
                            System.out.println(doctorDTO);
                            languages.clear();
                        }
                        case "cansel" -> {
                            execute(botService.enterLanguage(chatId.toString()));
                        }
                        case "save" -> {
                            execute(botService.enterExperience(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE);
                            userState = UserState.EXPERIENCE;
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
                            System.out.println("keldi");
                        }

                    }

                }

                case EXPERIENCE_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterAchievement(chatId.toString()));
                            doctorService.updateState(chatId, UserState.ACHIEVEMENT);
                            userState = UserState.ACHIEVEMENT;
                        }

                        case "add" -> {
                            userSelections.clear();
                            execute(botService.enterExperience(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EXPERIENCE);
                            userState = UserState.EXPERIENCE;
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
                            System.out.println("keldi");
                        }
                    }

                }

                case EDUCATION_FINISHED -> {
                    switch (data) {

                        case "save" -> {
                            execute(botService.enterReceptionAddress(chatId.toString()));
                            doctorService.updateState(chatId, UserState.RECEPTION_ADDRESS);
                            userState = UserState.RECEPTION_ADDRESS;
                        }

                        case "add" -> {
                            userSelections.clear();
                            execute(botService.enterEducation(chatId.toString()));
                            doctorService.updateState(chatId, UserState.EDUCATION);
                            userState = UserState.EDUCATION;
                        }

                    }
                }

            }
        }
    }

}
