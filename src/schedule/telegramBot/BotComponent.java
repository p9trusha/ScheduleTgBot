package schedule.telegramBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import schedule.models.Group;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * {@code token} is telegramBot token
 * <p>
 * {@code onUpdateReceived} check server
 * if server have update method would be start and give answers to user
 */
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
    UsersLog usersLog;
    public BotComponent(BotCommands botCommands, String token, HashMap<String, Group> schedule, UsersLog usersLog) {
        this.botCommands = botCommands;
        this.token = token;
        this.schedule = schedule;
        this.usersLog = usersLog;
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotAnswers botAnswers = new BotAnswers(schedule);
        String message = update.getMessage().getText().toLowerCase().strip();
        System.out.println(message + " ||| пользователь: " + update.getMessage().getFrom().getFirstName());
        message = message.replace("ё", "е");
        String groupPiece = "";
        String commandPiece = "";
        String text;
        String id = update.getMessage().getFrom().getId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        if (usersLog.getCommand(id) != null) {
            if (botAnswers.isGroup(message)) {
                groupPiece = message;
                commandPiece = usersLog.getCommand(id);
                usersLog.deleteCommand(id);
                try {
                    usersLog.newLog();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (message.startsWith("/")) {
            commandPiece = message;
        } else if (message.length() > 4) {
            groupPiece = message.substring(message.lastIndexOf(" ") + 1);
            commandPiece = message.substring(0, message.lastIndexOf(" ") + 1);
            commandPiece = commandPiece.strip();
        }
        if (botCommands.oneStepCommand.contains(commandPiece) && botAnswers.isGroup(groupPiece)
                || (botCommands.oneStepCommand.contains(commandPiece) && message.startsWith("/"))) {
            try {
                text = botAnswers.OneStepAnswers(update,
                        usersLog, botCommands, commandPiece, groupPiece);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            if (botCommands.twoStepCommand.contains(message)) {
                try {
                    text = botAnswers.TwoStepAnswers(update, botCommands, usersLog);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (botCommands.oneStepCommand.contains(commandPiece)){

                text = ("Сударь, вы ввели некорректно группу");

            }
            else {
                text = ("Сударь, вы ввели некорректно");
            }
        }
        sendMessage.setText(text);

        if (usersLog.getGroup(update.getMessage().getFrom().getId().toString()) != null) {
            ArrayList<String> arrayFastCommands = new ArrayList<>();
            for (String s : botCommands.twoStepCommand) {
                if (s.startsWith("мое")) {
                    arrayFastCommands.add(s);
                }
            }
            sendMessage.setReplyMarkup(botAnswers.replyKeyboardMarkup(arrayFastCommands));
        }
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}