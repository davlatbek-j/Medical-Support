package med.support.bot;

import lombok.RequiredArgsConstructor;
import med.support.constants.RestConstants;
import med.support.enums.UserState;
import med.support.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final DoctorService doctorService;

    private final BotService botService;

    private final RestTemplate restTemplate;
    private final Map<Long, UserState> userStates = new HashMap<>();

    public void setUserState(Long chatId, UserState state) {
        userStates.put(chatId, state);
    }

    public UserState getUserState(Long chatId) {
        return userStates.getOrDefault(chatId, UserState.START);
    }

    public static String getChatId(Update update) {
        return BotService.getChatId(update);
    }

    public static Long longChatId(Update update) {
        return BotService.longChatId(update);
    }

public void getUpdate(Update update){
    if (update.hasMessage() && update.getMessage().hasText()) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (message.equals("/start")) whenStart(update);
    }
}

    public void whenStart(Update update) {
        SendMessage sendMessage = new SendMessage(getChatId(update),
                "Выберите язык / Tilni tanlang:");
        sendMessage.setReplyMarkup(forStart());
        restTemplate.postForObject(RestConstants.FOR_MESSAGE, sendMessage, Object.class);
        setUserState(longChatId(update), UserState.SWITCH_LANGUAGE);
    }

    private InlineKeyboardMarkup forStart() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button1.setText("Русский");
        button2.setText("Ozbek ");

        button1.setCallbackData("Rus_tili");
        button2.setCallbackData("Ozbek_tili");

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(button1);
        row1.add(button2);
        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
