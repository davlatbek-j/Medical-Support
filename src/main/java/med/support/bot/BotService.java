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


//    private final DoctorRepository doctorRepository;
//    private final DoctorService doctorService;
//
//    private final RestTemplate restTemplate;
//    private final ValidationService validationService;
//
//    private final Map<Long, UserState> userStates = new HashMap<>();
//    private HashMap<Long, DoctorDTO> doctorsList = new HashMap<>();

    public SendMessage enterLogin(String chatId) {
        return new SendMessage(chatId, "Loginni kiriting: ");
    }

    public SendMessage enterPassword(String chatId) {
        return new SendMessage(chatId, "Parolni kiriting");
    }

    public SendMessage enterFirstName(String chatId) {
        return new SendMessage(chatId, "Ismingizni kiriting");
    }

    public SendMessage enterAgainPassword(String chatId) {
        return new SendMessage(chatId, "parol noto'g'ri iltimos qaytadan kiriting");
    }

    public SendMessage enterLastName(String chatId) {
        return new SendMessage(chatId, "Familyangizni kiriting");
    }

    public SendMessage enterSurname(String chatId) {
        return new SendMessage(chatId, "Surname kiriting");
    }

    public SendMessage enterPhone(String chatId) {
        return new SendMessage(chatId, "Telefon raqamingizni kiriting");
    }

    public SendMessage enterOutLine(String chatId) {
        return new SendMessage(chatId, "Outline kiriting");
    }

    public SendMessage enterMotto(String chatId) {
        return new SendMessage(chatId, "Motto kiriting");
    }

    public SendMessage validationMessage(String chatId, String validationMessage) {
        return new SendMessage(chatId, validationMessage);
    }

    public SendMessage sendPhoto(String chatId) {
        return new SendMessage(chatId, "Rasmingizni yuboring");
    }

    public SendMessage enterSpeciality(String chatId) {
        return new SendMessage(chatId, "Mutaxasisligingizni kiriting. " +
                "E'tibor bering agar sizning mutaxassisligingiz bir nechta" +
                " bo'lsa vergul bilan kiriting masalan: Nevrapatolik,Jarroh,...");
    }

    public SendMessage sendStart(String chatId) {
        return new SendMessage(chatId, "Boshlash uchun /start buyrug'ini kiriting");
    }

    public SendMessage enterAgainLogin(String chatId) {
        return new SendMessage(chatId, "Login noto'g'ri iltimos qaytadan kiritng");
    }

    public SendMessage enterLanguage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Qaysi tillarni bilishingizni tanlang:");
        sendMessage.setReplyMarkup(enterLanguageMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup enterLanguageMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("\uD83C\uDDFA\uD83C\uDDFFUZB");
        button.setCallbackData("uzb");
        rowInline.add(button);
        button = new InlineKeyboardButton();
        button.setText("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7FENG");
        button.setCallbackData("eng");
        rowInline.add(button);
        button = new InlineKeyboardButton();
        button.setText("\uD83C\uDDF7\uD83C\uDDFARUS");
        button.setCallbackData("rus");
        rowInline.add(button);

        rowsInline.add(rowInline);

        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Tasdiqlash");
        button1.setCallbackData("confirm");
        actionRow.add(button1);
        rowsInline.add(actionRow);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage sendSelectLanguageMessage(String chatId, String languages) {
        SendMessage sendMessage = new SendMessage(chatId, "Siz tanlagan tillar: " + languages);
        sendMessage.setReplyMarkup(sendSelectLanguageMessageMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup sendSelectLanguageMessageMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Bekor Qilish");
        button.setCallbackData("cansel");
        actionRow.add(button);
        button = new InlineKeyboardButton();
        button.setText("Saqlash");
        button.setCallbackData("save");
        actionRow.add(button);
        rowsInline.add(actionRow);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage enterExperience(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Tajribangizni kiriting");
        sendMessage.setReplyMarkup(experienceMarkup(Long.valueOf(chatId)));
        return sendMessage;
    }

    public InlineKeyboardMarkup experienceMarkup(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        Set<String> selections = MedicalBot.userSelections.getOrDefault(chatId, Collections.emptySet());

        if (!selections.contains("workplace")) {
            rowsInline.add(Collections.singletonList(createButton("Workplace", "workplace")));
        }
        if (!selections.contains("beginDate")) {
            rowsInline.add(Collections.singletonList(createButton("Begin Date", "beginDate")));
        }
        if (!selections.contains("endDate")) {
            rowsInline.add(Collections.singletonList(createButton("End Date", "endDate")));
        }
        if (!selections.contains("position")) {
            rowsInline.add(Collections.singletonList(createButton("Position", "position")));
        }

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }


//    public SendMessage enterExperienxe(String chatId) {
//        SendMessage sendMessage = new SendMessage(chatId, "Tajribangizni kiriting");
//        sendMessage.setReplyMarkup(experienceMarkup());
//        return sendMessage;
//    }
//
//    public InlineKeyboardMarkup experienceMarkup() {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//        InlineKeyboardButton button = new InlineKeyboardButton();
//        button.setText("Workplace");
//        button.setCallbackData("workplace");
//        rowsInline.add(List.of(button));
//
//        button = new InlineKeyboardButton();
//        button.setText("Begin Date");
//        button.setCallbackData("beginDate");
//        rowsInline.add(List.of(button));
//
//
//        button = new InlineKeyboardButton();
//        button.setText("End Date");
//        button.setCallbackData("endDate");
//        rowsInline.add(List.of(button));
//
//
//        button = new InlineKeyboardButton();
//        button.setText("Position");
//        button.setCallbackData("position");
//        rowsInline.add(List.of(button));
//
//
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//        return inlineKeyboardMarkup;
//    }

    public SendMessage enterWorkPlace(String chatId) {
        return new SendMessage(chatId, "Workplace kiriting");
    }

    public SendMessage enterBeginDate(String chatId) {
        return new SendMessage(chatId, "Begin date ni kiriting");
    }

    public SendMessage enterEndDate(String chatId) {
        return new SendMessage(chatId, "End Dateni kiriting");
    }

    public SendMessage enterPosition(String chatId) {
        return new SendMessage(chatId, "Positionni kiriting");
    }

//    public SendMessage enterExperienceWithoutWorkplace(String chatId) {
//        SendMessage sendMessage = new SendMessage(chatId, "Tajribangizni kiriting");
//        sendMessage.setReplyMarkup(experienceMarkup());
//        return sendMessage;
//    }
//    public InlineKeyboardMarkup experienceWithoutWorkplaceMarkup() {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//        InlineKeyboardButton button = new InlineKeyboardButton();
//        button.setText("Workplace");
//        button.setCallbackData("workplace");
//        rowsInline.add(List.of(button));
//
//        button = new InlineKeyboardButton();
//        button.setText("Begin Date");
//        button.setCallbackData("beginDate");
//        rowsInline.add(List.of(button));
//
//
//        button = new InlineKeyboardButton();
//        button.setText("End Date");
//        button.setCallbackData("endDate");
//        rowsInline.add(List.of(button));
//
//
//        button = new InlineKeyboardButton();
//        button.setText("Position");
//        button.setCallbackData("position");
//        rowsInline.add(List.of(button));
//
//
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//        return inlineKeyboardMarkup;
//    }
}


//    //STRING CHATID NI OLIB OLISH UCHUN
//    public static String getChatId(Update update) {
//        if (update.hasMessage()) {
//            return update.getMessage().getChatId().toString();
//        } else if (update.hasCallbackQuery()) {
//            return update.getCallbackQuery().getFrom().getId().toString();
//        }
//        return "";
//    }
//
//    // LONG CHATID NI OLISH UCHUN
//    public static Long longChatId(Update update) {
//        if (update.hasMessage()) {
//            return update.getMessage().getChatId();
//        } else if (update.hasCallbackQuery()) {
//            return update.getCallbackQuery().getMessage().getChatId();
//        }
//        return null;
//    }
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


//RUS TILIDA MALUMOTLARNI KIRITISH

//    public void saveUserUz(Update update) {
//        String chatId = getChatId(update);
//        new Doctor();
//        Doctor doctor;
//        if (doctorRepository.existsByChatId(chatId)) {
//            doctor = doctorRepository.findByChatId(chatId);
//            doctor.setState(UserState.LOGIN);
//        } else {
//            doctor = Doctor.builder().chatId(chatId).build();
//            doctor.setState(UserState.LOGIN);
//        }
//        doctorRepository.save(doctor);
//        loginUz(update);
//    }
//
//    public void loginUz(Update update) {
//
//        SendMessage sendMessage = new SendMessage(getChatId(update), "Sizga yuborilgan loginni kiriting ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        setUserState(longChatId(update), UserState.FIRSTNAME);
//
//    }
//
//
//    public void firstname(Update update) {
//        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
//        String login = update.getMessage().getText();
//        doctor.setLogin(login);
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//                "Tabriklaymiz siz botimiz xizmatlaridan foydalanishingiz mumkin \n" +
//                        "Ismingizni kiriting: ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        doctor.setState(UserState.LASTNAME);
//        doctorRepository.save(doctor);
//
//    }
//
//    public void lastname(Update update) {
//        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
//        String firstname = update.getMessage().getText();
//        doctor.setFirstname(firstname);
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//
//                        "Familiyangizni kiriting: ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//       doctor.setState(UserState.SURNAME);
//       doctorRepository.save(doctor);
//
//
//    }
//    public void surname(Update update){
//        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
//        String lastname = update.getMessage().getText();
//        doctor.setLastname(lastname);
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//
//                "Surnameni kiriting: ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        doctor.setState(UserState.OUTLINE);
//        doctorRepository.save(doctor);
//    }
//
//
//    public void outline(Update update){
//        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
//        String surname = update.getMessage().getText();
//        doctor.setSurname(surname);
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//
//                "Outline kiriting: ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        doctor.setState(UserState.MOTTO);
//        doctorRepository.save(doctor);
//    }
//    public void motto(Update update){
//        Doctor doctor = doctorRepository.findByChatId(getChatId(update));
//        String outline = update.getMessage().getText();
//        doctor.setOutline(outline);
//        SendMessage sendMessage = new SendMessage(getChatId(update),
//
//                "Motto kiriting: ");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
//        doctor.setState(UserState.MOTTO);
//        doctorRepository.save(doctor);
//    }
//
//
//    private void meRus(Update update) {
//        String chatId = getChatId(update);
//
//        Doctor doctor = doctorRepository.findByChatId(chatId);
//        String text = "*Sizning malumotlaringiz \n*"
//                + "Ism : " +
//                "\nFamiliya : " +
//                "\nSharif : " +
//                "\nTelefon raqam : " +
//                "\nKasbingiz : " +
//                "\ntajribangiz : " +
//                "\nManzilingiz : ";
//
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(chatId);
//        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        editMessageText.setReplyMarkup(forMeRus());
//        editMessageText.enableMarkdown(true);
//        editMessageText.setText(text);
//
//        restTemplate.postForObject(RestConstants.EDIT_MESSAGE, editMessageText, Object.class);
//        setUserState(longChatId(update), UserState.ALREADY_REGISTRATED);
//    }
//
//    private InlineKeyboardMarkup forMeRus() {
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        InlineKeyboardButton button1 = new InlineKeyboardButton();
//        InlineKeyboardButton button2 = new InlineKeyboardButton();
//
//        button1.setText("♻\uFE0FQayta ro'yxatdan o'tish");
//        button2.setText("To'lov qilish");
//
//        button1.setCallbackData("userReRegister");
//        button2.setCallbackData("to'lov");
//
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//
//        row1.add(button1);
//        row2.add(button2);
//
//        rowsInline.add(row1);
//        rowsInline.add(row2);
//
//        markupInline.setKeyboard(rowsInline);
//
//        return markupInline;
//
//    }


//--------------------------------------------------------------------------------------------------


