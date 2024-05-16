package med.support.bot;

import lombok.RequiredArgsConstructor;
import med.support.constants.RestConstants;
import med.support.entity.Doctor;
import med.support.enums.UserState;
import med.support.model.ApiResponse;
import med.support.model.DoctorDTO;
import med.support.model.LoginDTO;
import med.support.repository.DoctorRepository;
import med.support.service.DoctorService;
import med.support.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BotService {


    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    private final RestTemplate restTemplate;
    private final ValidationService validationService;

    private final Map<Long, UserState> userStates = new HashMap<>();
    private HashMap<Long, DoctorDTO> doctorsList = new HashMap<>();


    //STRING CHATID NI OLIB OLISH UCHUN
    public static String getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId().toString();
        }
        return "";
    }

    // LONG CHATID NI OLISH UCHUN
    public static Long longChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }

    public void setUserState(Long chatId, UserState state)
    {
        if (!doctorRepository.existsByChatId(String.valueOf(chatId))) {
            Doctor doctor = Doctor.builder()
                    .chatId(String.valueOf(chatId))
                    .state(state)
                    .build();
            doctorRepository.save(doctor);
        } else {
            Doctor doctor = doctorRepository.findByChatId(String.valueOf(chatId));
            doctor.setState(state);
            doctorRepository.save(doctor);
        }
    }

    public UserState getUserState(Long chatId) {
        Doctor doctor = doctorRepository.findByChatId(String.valueOf(chatId));
        if (doctor != null && doctor.getState() != null) {
            return doctor.getState();
        }
        return UserState.START;
    }


//RUS TILIDA MALUMOTLARNI KIRITISH

    public void saveUserUz(Update update) {
        String chatId = getChatId(update);
        new Doctor();
        Doctor doctor;
        if (doctorRepository.existsByChatId(chatId)) {
            doctor = doctorRepository.findByChatId(chatId);
            doctor.setState(UserState.LOGIN);
        } else {
            doctor = Doctor.builder().chatId(chatId).build();
            doctor.setState(UserState.LOGIN);
        }
        doctorRepository.save(doctor);
        loginUz(update);
    }

    public void loginUz(Update update) {

        SendMessage sendMessage = new SendMessage(getChatId(update), "Sizga yuborilgan loginni kiriting ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        setUserState(longChatId(update), UserState.FIRSTNAME);

    }


    public void firstname(Update update) {
        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
        String login = update.getMessage().getText();
        doctor.setLogin(login);
        SendMessage sendMessage = new SendMessage(getChatId(update),
                "Tabriklaymiz siz botimiz xizmatlaridan foydalanishingiz mumkin \n" +
                        "Ismingizni kiriting: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        doctor.setState(UserState.LASTNAME);
        doctorRepository.save(doctor);

    }

    public void lastname(Update update) {
        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
        String firstname = update.getMessage().getText();
        doctor.setFirstname(firstname);
        SendMessage sendMessage = new SendMessage(getChatId(update),

                        "Familiyangizni kiriting: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
       doctor.setState(UserState.SURNAME);
       doctorRepository.save(doctor);


    }
    public void surname(Update update){
        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
        String lastname = update.getMessage().getText();
        doctor.setLastname(lastname);
        SendMessage sendMessage = new SendMessage(getChatId(update),

                "Surnameni kiriting: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        doctor.setState(UserState.OUTLINE);
        doctorRepository.save(doctor);
    }


    public void outline(Update update){
        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
        String surname = update.getMessage().getText();
        doctor.setSurname(surname);
        SendMessage sendMessage = new SendMessage(getChatId(update),

                "Outline kiriting: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        doctor.setState(UserState.MOTTO);
        doctorRepository.save(doctor);
    }
    public void motto(Update update){
        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
        String outline = update.getMessage().getText();
        doctor.setOutline(outline);
        SendMessage sendMessage = new SendMessage(getChatId(update),

                "Motto kiriting: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        doctor.setState(UserState.MOTTO);
        doctorRepository.save(doctor);
    }


    private void meRus(Update update) {
        String chatId = getChatId(update);

        Doctor doctor = doctorRepository.findByChatId(chatId);
        String text = "*Sizning malumotlaringiz \n*"
                + "Ism : " +
                "\nFamiliya : " +
                "\nSharif : " +
                "\nTelefon raqam : " +
                "\nKasbingiz : " +
                "\ntajribangiz : " +
                "\nManzilingiz : ";

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setReplyMarkup(forMeRus());
        editMessageText.enableMarkdown(true);
        editMessageText.setText(text);

        restTemplate.postForObject(RestConstants.EDIT_MESSAGE, editMessageText, Object.class);
        setUserState(longChatId(update), UserState.ALREADY_REGISTRATED);
    }

    private InlineKeyboardMarkup forMeRus() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();

        button1.setText("♻\uFE0FQayta ro'yxatdan o'tish");
        button2.setText("To'lov qilish");

        button1.setCallbackData("userReRegister");
        button2.setCallbackData("to'lov");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(button1);
        row2.add(button2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);

        return markupInline;

    }


    //--------------------------------------------------------------------------------------------------
}

