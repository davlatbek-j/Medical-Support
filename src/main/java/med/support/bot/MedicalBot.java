package med.support.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import med.support.constants.RestConstants;
import med.support.entity.Doctor;
import med.support.enums.UserState;
import med.support.model.ApiResponse;
import med.support.model.DoctorDTO;
import med.support.model.LoginDTO;
import med.support.service.DoctorService;
import med.support.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MedicalBot extends TelegramLongPollingBot {

    private final DoctorService doctorService;

    private final ValidationService validationService;

    private final BotService botService;
    private final WebhookService webhookService;


    private UserState userState = null;

    private HashMap<Long, DoctorDTO> doctorsList = new HashMap<>();

//    private String login = "";
//    private String userPassword = "";
//    private String firstName = "";
//    private String lastName = "";
//    private String surname = "";
//    private String phone = "";
//    private String outLine = "";
//    private String motto = "";

    @Override
    public String getBotToken() {
        return RestConstants.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return RestConstants.BOT_USERNAME;
    }

    @SneakyThrows

    @Override
    public void onUpdateReceived(Update update) {

        //-------------------------------------------

        webhookService.getUpdate(update);
       /* //-------------------------------------------
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
                                doctorsList.put(chatId, DoctorDTO.builder().chatId(chatId).login(login).password(userPassword).build());
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
                    case LANGUAGE -> {

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
                            doctorService.saveChatId(login, chatId);
                            doctorService.updateState(chatId, UserState.LOGIN);
                            userState = UserState.LOGIN;
                            execute(botService.enterPassword(chatId.toString()));
                            return;
                        }
                        execute(botService.enterAgainLogin(chatId.toString()));
                        return;
                    }
                    execute(botService.enterLogin(chatId.toString()));
                }*/

//-----------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------

//                case LOGIN -> {
//                    String userPassword = text;
//                    String login = doctorService.findByChatId(chatId).get().getLogin();
//                    ResponseEntity<ApiResponse> result =
//                            doctorService.signIn(LoginDTO.builder().login(login).password(userPassword).build());
//                    System.out.println(result);
//                    if (result.getStatusCode().value() == 200) {
//                        execute(botService.enterFirstName(chatId.toString()));
//                        doctorService.updateState(chatId, UserState.FIRSTNAME);
//                        DoctorDTO doctorDTO = doctorsList.get(chatId);
//                        if (doctorDTO == null) {
//                            doctorsList.put(chatId, DoctorDTO.builder().chatId(chatId).login(login).password(userPassword).build());
//                        } else {
//                            doctorDTO.setLogin(login);
//                            doctorDTO.setPassword(userPassword);
//                            doctorsList.put(chatId, doctorDTO);
//                        }
//                        userState = UserState.FIRSTNAME;
//                        return;
//                    }
//                    execute(botService.enterAgainPassword(chatId.toString()));
//                    userState = UserState.LOGIN;
//                }
//                case FIRSTNAME -> {
//                    String validationMessage = validationService.validateNames(text, "Ism");
//                    if (validationMessage == null) {
//                        String firstName = text;
//                        execute(botService.enterLastName(chatId.toString()));
//                        doctorService.updateState(chatId, UserState.LASTNAME);
//                        userState = UserState.LASTNAME;
//                        DoctorDTO doctorDTO = doctorsList.get(chatId);
//                        doctorDTO.setFirstname(firstName);
//                        doctorsList.put(chatId, doctorDTO);
//                    } else {
//                        execute(botService.validationMessage(chatId.toString(), validationMessage));
//                    }
//                }
//
//                case LASTNAME -> {
//                    String validationMessage = validationService.validateNames(text, "Familya");
//                    if (validationMessage == null) {
//                        String lastName = text;
//                        execute(botService.enterSurname(chatId.toString()));
//                        doctorService.updateState(chatId, UserState.SURNAME);
//                        userState = UserState.SURNAME;
//                        DoctorDTO doctorDTO = doctorsList.get(chatId);
//                        doctorDTO.setLastname(lastName);
//                        doctorsList.put(chatId, doctorDTO);
//                    } else {
//                        execute(botService.validationMessage(chatId.toString(), validationMessage));
//                    }
//                }
//
//                case SURNAME -> {
//                    String validationMessage = validationService.validateNames(text, "Surname");
//                    if (validationMessage == null) {
//                        String surname = text;
//                        execute(botService.enterPhone(chatId.toString()));
//                        doctorService.updateState(chatId, UserState.PHONE);
//                        userState = UserState.PHONE;
//                        DoctorDTO doctorDTO = doctorsList.get(chatId);
//                        doctorDTO.setSurname(surname);
//                        doctorsList.put(chatId, doctorDTO);
//                    } else {
//                        execute(botService.validationMessage(chatId.toString(), validationMessage));
//                    }
//                }
//
//                case PHONE -> {
//                    String validationMessage = validationService.validatePhoneNumber(text);
//                    if (validationMessage == null) {
//                        String phone = text;
//                        execute(botService.enterOutLine(chatId.toString()));
//                        doctorService.updateState(chatId, UserState.OUTLINE);
//                        userState = UserState.OUTLINE;
//                        DoctorDTO doctorDTO = doctorsList.get(chatId);
//                        doctorDTO.setPhone(phone);
//                        doctorsList.put(chatId, doctorDTO);
//                    } else {
//                        execute(botService.validationMessage(chatId.toString(), validationMessage));
//                    }
//                }
//
//                case OUTLINE -> {
//                    String outLine = text;
//                    execute(botService.enterMotto(chatId.toString()));
//                    doctorService.updateState(chatId, UserState.MOTTO);
//                    userState = UserState.MOTTO;
//                    DoctorDTO doctorDTO = doctorsList.get(chatId);
//                    doctorDTO.setOutline(outLine);
//                    doctorsList.put(chatId, doctorDTO);
//                }
//
//                case MOTTO -> {
//                    String motto = text;
//                    execute(botService.sendPhoto(chatId.toString()));
//                    doctorService.updateState(chatId, UserState.PHOTO);
//                    userState = UserState.PHOTO;
//                    DoctorDTO doctorDTO = doctorsList.get(chatId);
//                    doctorDTO.setMotto(motto);
//                    doctorsList.put(chatId, doctorDTO);
//                }
//
//                case PHOTO -> {
//                    if (message.hasPhoto()) {
//                        execute(botService.enterSpeciality(chatId.toString()));
//                        userState = UserState.SPECIALITY;
//                    }
//                    execute(botService.sendPhoto(chatId.toString()));
//                }



    }
}
