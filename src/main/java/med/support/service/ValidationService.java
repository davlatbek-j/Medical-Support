package med.support.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class ValidationService {

    public String validateNames(String text, String textName) {
        if (!text.matches("^[A-Za-z]+$") || text.length() < 2 || text.length() > 50) {
            return textName + " faqat harflardan iborat bo'lishi kerak, va uzunligi 2 dan 50 gacha bo'lishi kerak.";
        }
        if (!Character.isUpperCase(text.charAt(0))) {
            return textName + " bosh harf bilan boshlanishi kerak.";
        }
        return null;
    }


    public String validatePhoneNumber(String phoneNumber) {
        // Regex to match Uzbek phone numbers: +998 followed by 9 digits
        String phoneRegex = "^\\+998[0-9]{9}$";
        if (!phoneNumber.matches(phoneRegex)) {
            return "Telefon raqami +998 bilan boshlanishi va 9 ta raqamdan iborat bo'lishi kerak.";
        }
        return null;
    }

    public boolean isValidYear(String yearStr) {
        return yearStr.matches("^(19|20)\\d{2}$");
    }

    public boolean isValidInteger(String numberStr) {
        return numberStr.matches("\\d+");
    }
    public boolean isValidContact(String input) {
        return input.matches("^@[A-Za-z0-9_]{5,32}$")  // Telegram username
                || input.matches("^(https?://)?(www.youtube.com/watch\\?v=[\\w-]+|youtu.be/[\\w-]+)$")  // YouTube link
                || input.matches("^[A-Za-z0-9_.+-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$");  // Email address
    }

    public String validateBeginDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);  // Bu sozlov noto'g'ri sanalarni rad etish uchun kerak
        try {
            sdf.parse(date);
            return null;  // Sana to'g'ri formatda
        } catch (ParseException e) {
            return "Sana noto'g'ri formatda kiritilgan." + dateFormat + "" +
                    "\n"+"To'g'ri format: (masalan, 31/12/2024)";
        }
    }


}
