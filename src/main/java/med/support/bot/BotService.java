package med.support.bot;

import lombok.RequiredArgsConstructor;
import med.support.model.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotService {

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
        return new SendMessage(chatId, "O'zingiz haqingizda qisqacha ma'lumot kiriting");
    }

    public SendMessage enterMotto(String chatId) {
        return new SendMessage(chatId, "Shioringizni kiriting");
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
            rowsInline.add(Collections.singletonList(createButton("Ishlagan joyingiz nomi", "workplace")));
        }
        if (!selections.contains("beginDate")) {
            rowsInline.add(Collections.singletonList(createButton("Ish boshlagan sanangiz", "beginDate")));
        }
        if (!selections.contains("endDate")) {
            rowsInline.add(Collections.singletonList(createButton("Qachongacha ishlagansiz(sana)", "endDate")));
        }
        if (!selections.contains("position")) {
            rowsInline.add(Collections.singletonList(createButton("Lavozimingiz", "position")));
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


    public SendMessage enterWorkPlace(String chatId) {
        return new SendMessage(chatId, "Ishlagan joyingiz nomini kiriting");
    }

    public SendMessage enterBeginDate(String chatId) {
        return new SendMessage(chatId, "U yerda ish boshlagan sanangizni kiriting");
    }

    public SendMessage enterEndDate(String chatId) {
        return new SendMessage(chatId, "U yerda faoliyatingizni to'xtatgan sanangizni kiriting");
    }

    public SendMessage enterPosition(String chatId) {
        return new SendMessage(chatId, "Qanday lavozimda ishlaganingizni kiriting");
    }

    public SendMessage isSaveExperience(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Agarda yana qo'shimcha ishlagan joyingiz bo'lsa kiriting. Yo'q bo'lsa saqlashni bosing");
        sendMessage.setReplyMarkup(isSaveExperienceMarkup());
        return sendMessage;
    }

    private InlineKeyboardMarkup isSaveExperienceMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Saqlash");
        button.setCallbackData("save");
        actionRow.add(button);
        button = new InlineKeyboardButton();
        button.setText("Yana➕");
        button.setCallbackData("add");
        actionRow.add(button);
        rowsInline.add(actionRow);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage enterAchievement(String chatId) {
        return new SendMessage(chatId, "Erishgan yutuqlaringizni kiriting");
    }

    public SendMessage enterEducation(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "O'qigan joyingiz ma'lumotlarini kiriting");
        sendMessage.setReplyMarkup(enterEducationMarkup(Long.valueOf(chatId)));
        return sendMessage;
    }

    private InlineKeyboardMarkup enterEducationMarkup(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        Set<String> selections = MedicalBot.userSelections.getOrDefault(chatId, Collections.emptySet());

        if (!selections.contains("name")) {
            rowsInline.add(Collections.singletonList(createButton("Oliygoh nomi", "name")));
        }
        if (!selections.contains("startYear")) {
            rowsInline.add(Collections.singletonList(createButton("O'qishni boshlagan yilingiz", "startYear")));
        }
        if (!selections.contains("endYear")) {
            rowsInline.add(Collections.singletonList(createButton("O'qishni tugatgan yilingiz", "endYear")));
        }
        if (!selections.contains("faculty")) {
            rowsInline.add(Collections.singletonList(createButton("Fakultet nomi", "faculty")));
        }

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    public SendMessage isSaveEducation(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Agarda yana qo'shimcha o'qigan joyingiz bo'lsa kiriting. Yo'q bo'lsa saqlashni bosing");
        sendMessage.setReplyMarkup(isSaveExperienceMarkup());
        return sendMessage;
    }

    public SendMessage enterReceptionAddress(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Reception address kiriting");
        sendMessage.setReplyMarkup(receptionMarkum(Long.valueOf(chatId)));
        return sendMessage;
    }

    public InlineKeyboardMarkup receptionMarkum(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        Set<String> selections = MedicalBot.userSelections.getOrDefault(chatId, Collections.emptySet());

        if (!selections.contains("addressName")) {
            rowsInline.add(Collections.singletonList(createButton("Address name", "addressName")));
        }
        if (!selections.contains("addressUrl")) {
            rowsInline.add(Collections.singletonList(createButton("Address url", "addressUrl")));
        }


        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    public SendMessage enterEducationName(String chatId) {
        return new SendMessage(chatId, "Education name kiriting");
    }

    public SendMessage enterEducationStartYear(String chatId) {
        return new SendMessage(chatId, "U yerda qaysi yili o'qishni boshlagansiz. Faqat yil raqamlarini kiriting. Masalan: 2024");
    }

    public SendMessage enterEducationEndYear(String chatId) {
        return new SendMessage(chatId, "U yerda qaysi yili o'qishni Tugatgansiz. Faqat yil raqamlarini kiriting. Masalan: 2024");
    }

    public SendMessage enterEducationFaculty(String chatId) {
        return new SendMessage(chatId, "Qaysi fakultetini tamomlagansiz");
    }


    public SendMessage enterAddressName(String chatId) {
        return new SendMessage(chatId, "Addres nomini kiriting");
    }

    public SendMessage enterAddressUrl(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Address Locatsiyasini yuboring");
        return sendMessage;
    }

    public SendMessage isSaveReceptionAddress(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Agarda yana qo'shimcha reception address bo'lsa kiriting. Yo'q bo'lsa saqlashni bosing");
        sendMessage.setReplyMarkup(isSaveExperienceMarkup());
        return sendMessage;
    }

    public SendMessage enterServices(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Service kiriting");
        sendMessage.setReplyMarkup(serviceMarkup(Long.valueOf(chatId)));
        return sendMessage;
    }

    public InlineKeyboardMarkup serviceMarkup(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        Set<String> selections = MedicalBot.userSelections.getOrDefault(chatId, Collections.emptySet());

        if (!selections.contains("serviceName")) {
            rowsInline.add(Collections.singletonList(createButton("Service name", "serviceName")));
        }
        if (!selections.contains("servicePrice")) {
            rowsInline.add(Collections.singletonList(createButton("Service Price", "servicePrice")));
        }


        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    public SendMessage isSaveService(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Agarda yana qo'shimcha service bo'lsa kiriting. Yo'q bo'lsa saqlashni bosing");
        sendMessage.setReplyMarkup(isSaveExperienceMarkup());
        return sendMessage;
    }

    public SendMessage enterContact(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "contact kiriting");
        sendMessage.setReplyMarkup(contactMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup contactMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Telegram");
        button.setCallbackData("telegram");
        rowInline.add(button);
        button = new InlineKeyboardButton();
        button.setText("You tube");
        button.setCallbackData("youTube");
        rowInline.add(button);
        button = new InlineKeyboardButton();
        button.setText("Mail");
        button.setCallbackData("mail");
        rowInline.add(button);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage enterServiceName(String chatId) {
        return new SendMessage(chatId, "Service nomini kiriting");
    }

    public SendMessage enterServicePrice(String chatId) {
        return new SendMessage(chatId, "Service price kiriting. Price faqat sonlarda iborat bo'lsin.Masalan:100000");
    }

    public SendMessage enterContactValue(String chatId) {
        return new SendMessage(chatId, "Tanlagan contactga mos username yoki link kiriting");
    }

    public SendMessage saveContact(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Agarda yana qo'shimcha service bo'lsa kiriting. Yo'q bo'lsa saqlashni bosing");
        sendMessage.setReplyMarkup(isSaveExperienceMarkup());
        return sendMessage;

    }

    public SendMessage finished(String chatId) {
        return new SendMessage(chatId, "Tabriklaymiz muvaffaqqiyatli ro'yxatdan o'tdingiz");
    }

    public SendMessage enterTrueYear(String chatId) {
        return new SendMessage(chatId, "Iltimos, to'g'ri yil kiriting (masalan, 1999)");
    }

    public SendMessage enterTruePrice(String chatId) {
        return new SendMessage(chatId, "Iltimos, to'g'ri narx kiriting (faqat raqamlar).");
    }

    public SendMessage enterTrueContact(String chatId) {
        return new SendMessage(chatId, "Iltimos, to'g'ri aloqa ma'lumotlarini kiriting: Telegram @username, YouTube linki, yoki email manzili.");
    }

    public SendPhoto sendAllInformation(String chatId, DoctorDTO doctorDTO, String fileId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(fileId));
        String caption = String.format(
                "First Name: %s\n" +
                        "Last Name: %s\n" +
                        "Surname: %s\n" +
                        "Phone: %s\n" +
                        "Qisqacha ma'lumot: %s\n" +
                        "Shior: %s\n" +
                        "Mutaxassislik: %s\n" +
                        "Til: %s\n" +
                        "Erishgan yutuqlari: %s\n" +
                        "Ish tajribasi: %s\n" +
                        "Qayerda o'qigan: %s\n" +
                        "Reception Address: %s\n" +
                        "Services Offered: %s\n" +
                        "Contact ma'lumoti: %s",
                doctorDTO.getFirstname(),
                doctorDTO.getLastname(),
                doctorDTO.getSurname(),
                doctorDTO.getPhone(),
                doctorDTO.getOutline(),
                doctorDTO.getMotto(),
                doctorDTO.getSpecialty(),
                doctorDTO.getLanguage(),
                doctorDTO.getAchievement(),
                formatExperience(doctorDTO.getExperience()),
                formatEducation(doctorDTO.getEducation()),
                formatReceptionAddress(doctorDTO.getReceptionAddress()),
                formatServices(doctorDTO.getService()),
                formatContactInfo(doctorDTO.getContact())
        );
        sendPhoto.setCaption(caption);
        return sendPhoto;
    }

    // Bu metodlar DTO ob'ektlarining ma'lumotlarini chiroyli formatga o'tkazadi
    private String formatExperience(Set<ExperienceDTO> experiences) {
        return experiences.stream()
                .map(e -> String.format("%s (%s - %s)", e.getWorkplace(), e.getBeginDate(), e.getEndDate()))
                .collect(Collectors.joining("\n"));
    }

    private String formatEducation(Set<EducationDTO> educationList) {
        return educationList.stream()
                .map(e -> String.format("%s (%d - %d)", e.getName(), e.getStartYear(), e.getEndYear()))
                .collect(Collectors.joining("\n"));
    }

    private String formatReceptionAddress(Set<ReceptionAddressDTO> addresses) {
        return addresses.stream()
                .map(a -> String.format("%s [%s]", a.getAddressName(), a.getHttpUrl()))
                .collect(Collectors.joining("\n"));
    }

    private String formatServices(Set<ServiceDTO> services) {
        return services.stream()
                .map(s -> String.format("%s - %s", s.getName(), s.getPrice()))
                .collect(Collectors.joining("\n"));
    }

    private String formatContactInfo(Set<ContactDTO> contacts) {
        return contacts.stream()
                .map(c -> String.format("%s: %s", c.getContactType(), c.getValue()))
                .collect(Collectors.joining("\n"));
    }

    public SendMessage sendBedinDateMessage(String chatId, String validationResponse) {
        return new SendMessage(chatId, validationResponse);
    }

    public SendMessage alreadyExistsMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Siz ro'yxatdan o'tgansiz. Qayatadan ro'yxatdan o'tmoqchimisiz?");
        sendMessage.setReplyMarkup(alreadyExistsMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup alreadyExistsMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Ha");
        button.setCallbackData("yes");
        actionRow.add(button);
        button = new InlineKeyboardButton();
        button.setText("Yo'q");
        button.setCallbackData("no");
        actionRow.add(button);
        rowsInline.add(actionRow);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage sendMessageForNo(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Sizga keluvchi xabarlarlar shu accontga kelsinmi yoki eski accontgami?");
        sendMessage.setReplyMarkup(forNoMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup forNoMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Yangi");
        button.setCallbackData("new");
        actionRow.add(button);
        button = new InlineKeyboardButton();
        button.setText("Eski");
        button.setCallbackData("old");
        actionRow.add(button);
        rowsInline.add(actionRow);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage sendMessageForNew(String chatId) {
        return new SendMessage(chatId, "Assosiy accountingiz o'zgardi");
    }

    public SendMessage sendMessageForOld(String chatId) {
        return new SendMessage(chatId, "Asosiy accountingiz o'zgarishsiz qoldirildi");
    }

    public SendMessage sendInvalidWorkplaceMessage(String chatId) {
        return new SendMessage(chatId,"Siz kiritgan nom faqat harflar va bo'sh joylardan iborat bo'lish kerak. Hamda Eng kamida 3 ta belgidan iborat bo'lsin!");
    }

    public SendMessage enterInvalidSpecialityMessage(String chatId) {
        return new SendMessage(chatId,"Har bir so'z bosh va oxirida bo'sh joylarsiz bo'lishi " +
                "va harflardan iborat bo'lishi kerak. Agar mutaxassislik nomida bo'sh joylar bo'lsa, " +
                "faqat so'zlar orasida bo'lishi kerak.");
    }

    public SendMessage enterInvalidAchievementMessage(String chatId) {
        String message = "Kechirasiz, kiritgan yutuqlaringiz formati noto'g'ri. " +
                "Iltimos, yutuqlaringizni aniq va to'liq ko'rinishda qayta yozing. " +
                "Yutuq matni kamida 10 ta belgidan iborat bo'lishi kerak va maxsus belgilarni " +
                "(masalan, <, >, {, }) o'z ichiga olmasligi kerak. " +
                "Shuningdek, matn boshida yoki oxirida bo'sh joylar bo'lmasligi kerak.";
        return new SendMessage(chatId, message);
    }

}
