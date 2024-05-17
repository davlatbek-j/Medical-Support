//package med.support.bot;
//
//import lombok.RequiredArgsConstructor;
//import med.support.constants.RestConstants;
//import med.support.entity.Doctor;
//import med.support.enums.UserState;
//import med.support.repository.DoctorRepository;
//import med.support.service.DoctorService;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class WebhookService {
//    private final DoctorService doctorService;
//
//    private final BotService botService;
//
//    private final RestTemplate restTemplate;
//
//    private final DoctorRepository doctorRepository;
//
//    public void setUserState(Long chatId, UserState state)
//    {
//        if (!doctorRepository.existsByChatId(String.valueOf(chatId))) {
//            Doctor doctor = Doctor.builder()
//                    .chatId(String.valueOf(chatId))
//                    .state(state)
//                    .build();
//            doctorRepository.save(doctor);
//        } else {
//            Doctor doctor = doctorRepository.findByChatId(String.valueOf(chatId));
//            doctor.setState(state);
//            doctorRepository.save(doctor);
//        }
//    }
//
//    public UserState getUserState(Long chatId) {
//        Doctor doctor = doctorRepository.findByChatId(String.valueOf(chatId));
//        if (doctor != null && doctor.getState() != null) {
//            return doctor.getState();
//        }
//        return UserState.START;
//    }
//
//    public static String getChatId(Update update) {
//        return BotService.getChatId(update);
//    }
//
//    public static Long longChatId(Update update) {
//        return BotService.longChatId(update);
//    }
//
//    public void getUpdate(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String message = update.getMessage().getText();
//            Long chatId = update.getMessage().getChatId();
//            if (message.equals("/start")) whenStart(update);
//
//       /* else if (botService.getUserState(chatId).equals(UserState.LOGIN)) {
//                 botService.enterLogin(update);
//        }*/
//
//            if (botService.getUserState(chatId).equals(UserState.LOGIN)) {
//                botService.loginUz(update);
//            }
//
//             else if (botService.getUserState(chatId).equals(UserState.INCORRECT_LOGIN)) {
//                botService.firstname(update);
//            }  else if (botService.getUserState(chatId).equals(UserState.CHECK_PASSWORD)) {
//                botService.firstname(update);
//            }else if (botService.getUserState(chatId).equals(UserState.FIRSTNAME)) {
//                botService.firstname(update);
//            }else if (botService.getUserState(chatId).equals(UserState.LASTNAME)) {
//                botService.lastname(update);
//            }else if (botService.getUserState(chatId).equals(UserState.SURNAME)) {
//                botService.surname(update);
//            }else if (botService.getUserState(chatId).equals(UserState.OUTLINE)) {
//                botService.outline(update);
//            }
//        }
//        else if (update.hasCallbackQuery()) {
//            String data = update.getCallbackQuery().getData();
//            Long chatId = update.getCallbackQuery().getMessage().getChatId();
//            if (getUserState(chatId).equals(UserState.SWITCH_LANGUAGE) && data.equals("Rus_tili")) {
//                //botService.saveUserRus(update);
////
//            } else if (getUserState(chatId).equals(UserState.SWITCH_LANGUAGE) && data.equals("Ozbek_tili")) {
//                botService.saveUserUz(update);
//            }
//        }
//    }
//
//    public void whenStart(Update update) {
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//                "Выберите язык / Tilni tanlang:");
//        sendMessage.setReplyMarkup(forStart());
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        setUserState(longChatId(update), UserState.SWITCH_LANGUAGE);
//    }
//
//    private InlineKeyboardMarkup forStart() {
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        InlineKeyboardButton button1 = new InlineKeyboardButton();
//        InlineKeyboardButton button2 = new InlineKeyboardButton();
//        button1.setText("\uD83C\uDDF7\uD83C\uDDFA Русский");
//        button2.setText("\uD83C\uDDF8\uD83C\uDDF1 O'zbek ");
//
//        button1.setCallbackData("Rus_tili");
//        button2.setCallbackData("Ozbek_tili");
//
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//
//        row1.add(button1);
//        row1.add(button2);
//        rowsInline.add(row1);
//
//        markupInline.setKeyboard(rowsInline);
//        return markupInline;
//    }
//
//}
