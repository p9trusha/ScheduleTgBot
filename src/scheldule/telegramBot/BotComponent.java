package scheldule.telegramBot;


import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;




public class BotComponent extends TelegramLongPollingBot{

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
    public BotComponent(BotCommands botCommands, String token) {
        this.botCommands = botCommands;
        this.token = token;
    }
    public String withOutSpace(String s){
        while (s.endsWith(" ")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Проверим, работает ли наш бот.

        String chatid = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String endOfMessage = message.substring(message.length()-4);
        String startOfMessage = message.substring(0, message.length() - 4);
        startOfMessage = withOutSpace(startOfMessage);

        if (botCommands.oneStepCommand.contains(message) && message.startsWith("/")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatid);
            sendMessage.setText("Доступные команды:\n/start\nРасписание на завтра ****\nРасписание на завтра");
            try {
                this.execute(sendMessage);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        else if (StringUtils.isNumeric(endOfMessage)) {
            if (botCommands.oneStepCommand.contains(startOfMessage)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatid);
                sendMessage.setText("Расписание на завтра я откуда ебу");
                try {
                    this.execute(sendMessage);

                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if (botCommands.twoStepCommand.contains(message)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatid);
            sendMessage.setText("Введите группу");
            try {
                this.execute(sendMessage);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatid);
            sendMessage.setText("Сударь, вы ввели хуйню");
            try {
                this.execute(sendMessage);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

