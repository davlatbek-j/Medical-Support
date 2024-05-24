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

//    public boolean isValidEndYear(String startYear, String endYear) {
//        if (!isValidYear(startYear) || !isValidYear(endYear)) {
//            return false;
//        }
//        int start = Integer.parseInt(startYear);
//        int end = Integer.parseInt(endYear);
//        return end >= start;
//    }

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
    public boolean validateWorkplace(String workplace) {
        if (workplace == null || workplace.isEmpty()) {
            return false;
        }
        // Validate that the workplace contains only letters and spaces and has a minimum length of 3 characters.
        return workplace.matches("[a-zA-Z\\s]{3,}");
    }

    public boolean isValidSpeciality(String speciality) {
        if (speciality == null || speciality.isEmpty()) {
            return false;
        }
        // Validate that each speciality contains only letters and possibly spaces (but not leading or trailing spaces)
        String[] parts = speciality.split(",");
        for (String part : parts) {
            if (!part.trim().matches("[a-zA-Z\\s]+")) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidAchievement(String achievement) {
        if (achievement == null || achievement.trim().isEmpty()) {
            return false;
        }
        // Validate that the achievement text is between 10 and 500 characters and does not start or end with whitespace
        if (achievement.length() < 10 || achievement.length() > 500) {
            return false;
        }
        // Optionally, add more specific checks, for example, forbidding certain special characters
        return !achievement.matches(".*[{}<>].*"); // Example to forbid <, >, {, }
    }

}
