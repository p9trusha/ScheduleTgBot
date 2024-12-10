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
    public static void main(String[] args)  {
        String tokenPath = "token.txt";
        String commandsPath = "botCommands.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(tokenPath))){
            try (BufferedReader commandReader = new BufferedReader(new FileReader(commandsPath))) {
            String token;
            token = reader.readLine();
            BotCommands botCommands = getBotCommands(commandReader);
            String jsonContent = EtuApi.getJson();
            HashMap<String, Group> schedule = Parser.json(jsonContent);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new BotComponent(botCommands, token, schedule));}

        } catch (IOException FileNotFoundException) {
            System.out.println("Файл не удалось открыть");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static BotCommands getBotCommands(BufferedReader commandReader) throws IOException {
        BotCommands botCommands = new BotCommands();
        int count;
        count = Integer.parseInt(commandReader.readLine());
        for (int i = 0; i < count; i++) {
            botCommands.addOneStepCommand(commandReader.readLine());
        }
        count = Integer.parseInt(commandReader.readLine());
        for (int i =0; i < count; i++) {
            botCommands.addTwoStepComand(commandReader.readLine());
        }
        return botCommands;
    }
}


