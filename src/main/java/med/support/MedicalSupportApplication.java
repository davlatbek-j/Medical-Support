package med.support;

import med.support.bot.BotInitializer;
import med.support.bot.MedicalBot;
import org.hibernate.sql.results.graph.collection.internal.BagInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class MedicalSupportApplication
{
    public static void main(String[] args) {
        SpringApplication.run(MedicalSupportApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public TelegramBotsApi telegramBotsApi(MedicalBot medicalBot) throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi=new TelegramBotsApi(DefaultBotSession.class);
//        telegramBotsApi.registerBot(medicalBot);
//        return telegramBotsApi;
//    }

}
