package schedule.telegramBot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import schedule.models.Group;

import java.util.HashMap;


public class BotComponent extends TelegramLongPollingBot{

    HashMap<String, Group> schedule;

    @Override
    public String getBotUsername() {
        return "schedule_etu3354_bot";
    }
    String token;


    @Override
    public String getBotToken() {
        return token;
    }
    BotCommands botCommands;
    public BotComponent(BotCommands botCommands, String token, HashMap<String, Group> schedule) {
        this.botCommands = botCommands;
        this.token = token;
        this.schedule = schedule;
    }
    public String withOutSpace(String s){
        while (s.endsWith(" ")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
    BotAnswers botAnswers = new BotAnswers();
    @Override
    public void onUpdateReceived(Update update) {
        //Проверим, работает ли наш бот.
        String message = update.getMessage().getText();
        String groupPiece = "";
        String commandPiece = "";
        if (message.startsWith("/")) {
            commandPiece = message;
        } else if (message.length() > 4) {
            groupPiece = message.substring(message.length()-4);
            commandPiece = message.substring(0, message.length() - 4);
            commandPiece = withOutSpace(commandPiece);
        }
        if (botCommands.oneStepCommand.contains(commandPiece)) {
             {

                 try {
                     this.execute(botAnswers.OneStepAnswers(update, botCommands, commandPiece, groupPiece, schedule));
                 } catch (TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
             }
        }
        else if (botCommands.twoStepCommand.contains(message)) {

            try {
                this.execute(botAnswers.TwoStepAnswers(update, botCommands, commandPiece, groupPiece, schedule));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setText("Сударь, вы ввели некорректно");
            try {
                this.execute(sendMessage);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

