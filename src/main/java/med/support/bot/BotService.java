package med.support.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;

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
        return new SendMessage(chatId, "Outline kiriting");
    }

    public SendMessage enterMotto(String chatId) {
        return new SendMessage(chatId, "Motto kiriting");
    }

    public SendMessage validationMessage(String chatId, String validationMessage) {
        return new SendMessage(chatId, validationMessage);
    }

    public SendMessage sendPhoto(String chatId) {
        return new SendMessage(chatId,"Rasmingizni yuboring");
    }

    public SendMessage enterSpeciality(String chatId) {
        return new SendMessage(chatId,"Mutaxasisligingizni kiriting. " +
                "E'tibor bering agar sizning mutaxassisligingiz bir nechta" +
                " bo'lsa vergul bilan kiriting masalan: Nevrapatolik,Jarroh,...");
    }

    public SendMessage sendStart(String chatId) {
        return new SendMessage(chatId, "Boshlash uchun /start buyrug'ini kiriting");
    }

    public SendMessage enterAgainLogin(String chatId) {
        return new SendMessage(chatId,"Login noto'g'ri iltimos qaytadan kiritng");
    }

    public SendMessage enterLanguage(String chatId) {
        return new SendMessage(chatId,"Tilni kirit ovsar");
    }
}
