package med.support.constants;

public interface RestConstants {

    String BOT_TOKEN = "6571991445:AAF2d7a4-xislEw44BCclTiQXrxgks2tSbA";
    String BOT_USERNAME = "t.me/medi_register_bot";
    String URL = "https://api.telegram.org/bot";
    String FOR_MESSAGE = URL + BOT_TOKEN + "/sendMessage";
    String EDIT_MESSAGE = URL + BOT_TOKEN + "/editMessageText";
    String FORWARD = URL + BOT_TOKEN + "/forwardMessage";
    String FOR_DELETE_MESSAGE = URL + BOT_TOKEN + "/deleteMessage";
    String FOR_DOCUMENT = URL + BOT_TOKEN + "/sendDocument";
}
