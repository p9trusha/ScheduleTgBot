package schedule;

import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.HashMap;

import schedule.telegramBot.BotCommands;
import schedule.telegramBot.BotComponent;



class EtuApi {
    static String getJson() throws IOException {
        String apiLink = "https://digital.etu.ru/api/mobile/schedule";
        return IOUtils.toString(URI.create(apiLink).toURL(), StandardCharsets.UTF_8);
    }
}

/**
 * comment
 */
public class Main {

    /**
     * class main
     * wow
     */
    public static void main(String[] args) throws TelegramApiException {
        String tokenPath = "token.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(tokenPath))){
            String token;
            token = reader.readLine();
            BufferedReader reader1 = new BufferedReader(new FileReader("botComands.txt"));
            BotCommands botComands = new BotCommands();
            int count;
            count = Integer.parseInt(reader1.readLine());
            for (int i = 0; i < count; i++) {
                botComands.addOneStepCommand(reader1.readLine());
            }
            count = Integer.parseInt(reader1.readLine());
            for (int i =0; i < count; i++) {
                botComands.addTwoStepComand(reader1.readLine());
            }
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new BotComponent(botComands, token));
            String jsonContent = EtuApi.getJson();
            HashMap<String, Group> schedule = Parser.json(jsonContent);
        } catch (IOException FileNotFoundException) {
            System.out.println("Файл не удалось открыть");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}


