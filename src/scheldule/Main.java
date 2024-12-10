package scheldule;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.*;

import scheldule.telegramBot.BotCommands;
import scheldule.telegramBot.BotComponent;

class EtuApi {
    String getJson(String group) throws IOException {
        group = URLEncoder.encode(group, StandardCharsets.UTF_8);
        String apiLink = String.format(
                "https://https://digital.etu.ru/api/mobile/schedule",
                group
        );
        URL searchReguest = new URL(apiLink);
        URLConnection urlCon = searchReguest.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlCon.getInputStream()));
        String inputLine;
        inputLine = in.readLine();
        in.close();
        return (inputLine);
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

        } catch (IOException FileNotFoundException) {
            System.out.println("Файл не удалось открыть");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}


