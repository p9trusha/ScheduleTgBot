package schedule.telegramBot;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import schedule.models.Group;
import schedule.models.Lesson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class BotAnswers {
    String daySchedule(ArrayList<Lesson> lessons, int week, long dayTimestamp) {
        StringBuilder text = new StringBuilder();
        int weekParity;
        if (week % 2 == 0) {
            weekParity = 2;
        }
        else {
            weekParity = 1;
        }
        for (Lesson lesson : lessons) {
            if (lesson.getWeek() == weekParity) {
                text.append(lesson.toString(dayTimestamp));
            }
        }
    return text.toString();
    }

    StringBuilder startCommand(BotCommands botCommands) {
        StringBuilder text = new StringBuilder("Приветствуем в нашем боте, доступны такие команды:\nОдноступенчатые\n");
        for (int i = 0; i < botCommands.oneStepCommand.size(); i++) {
            if (botCommands.oneStepCommand.get(i).startsWith("/")) {
                text.append(botCommands.oneStepCommand.get(i)).append('\n');
            }
            else {
            text.append(botCommands.oneStepCommand.get(i)).append(" [Номер группы]").append('\n');}
        }
        text.append("Двухступенчатые:\n");
        for (int i = 0; i < botCommands.twoStepCommand.size(); i++) {
            text.append(botCommands.twoStepCommand.get(i)).append('\n');
        }
        return text;
    }
    String infoCommand() {
        return """
                Бот является курсовой работой Неволина П. и Махмудова М.\
                
                Неволин отвечает за парсировку расписания\
                
                Махмудов за работу телеграм бота\
                
                Курсовая написана на языке java (жаль не пайтон)\
                
                Используются такие библиотеки как gson и telegramBots""";
    }

    SendMessage OneStepAnswers(Update update, UsersLog usersLog, BotCommands botCommands, String command,
                               String groupNumber, HashMap<String, Group> schedule) throws IOException {

        String chatId = update.getMessage().getChatId().toString();
        String text = "";
        Calendar calendar = new GregorianCalendar();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (calendar.get(Calendar.MONTH) >= 9) {
            week -= new GregorianCalendar(calendar.get(Calendar.YEAR),
                    Calendar.SEPTEMBER,
                    1).get(Calendar.WEEK_OF_YEAR);
        }
        else {
            week -= new GregorianCalendar(calendar.get(Calendar.YEAR) - 1,
                    Calendar.SEPTEMBER,
                    1).get(Calendar.WEEK_OF_YEAR);
        }
        UsersLog.UserLog userLog = new UsersLog.UserLog();
        if (command.toLowerCase().equals(OneStepBotCommands.START.getTitle())) {
            text = String.valueOf(startCommand(botCommands));
        }
        else if (command.toLowerCase().equals(OneStepBotCommands.INFO.getTitle())) {
            text = infoCommand();
        }
        else if (!groupNumber.isEmpty()) {
            if (StringUtils.isNumeric(groupNumber) && (schedule.containsKey(groupNumber))) {
                if (command.toLowerCase().equals(OneStepBotCommands.TODAY__GROUP_NUMBER.getTitle())) {
                    int today = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                    ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays().getDayOfWeek(today).getLessons();
                    text = String.format("Расписание на %d.%d\n%s",
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.MONTH),
                            daySchedule(lessons, week, getTimestamp(calendar)));
                }
                else if (command.toLowerCase().equals(OneStepBotCommands.TOMORROW__GROUP_NUMBER.getTitle())) {
                    calendar.roll(Calendar.DAY_OF_YEAR, 1);
                    int tomorrow = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                    if (tomorrow == 0) { week++; }
                    if (StringUtils.isNumeric(groupNumber) && (schedule.containsKey(groupNumber))) {
                        ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays()
                                .getDayOfWeek(tomorrow).getLessons();
                        text = daySchedule(lessons, week, getTimestamp(calendar));
                        text = String.format("Расписание на %d.%d\n%s",
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH),
                                text);
                    }
                    calendar.roll(Calendar.DAY_OF_YEAR, -1);
                }
                else if (command.toLowerCase().equals(OneStepBotCommands.THIS_WEEK__GROUP_NUMBER.getTitle())) {
                    int indexDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                    calendar.roll(Calendar.DAY_OF_YEAR, -indexDayOfWeek);
                    for (int i = 0; i < 7; i++) {
                        if (!schedule.get(groupNumber).getDays().getDayOfWeek(i).getLessons().isEmpty()) {
                            ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays()
                                    .getDayOfWeek(i).getLessons();
                            text = String.format("%s%s\n%s\n",
                                    text, schedule.get(groupNumber).getDays().getDayOfWeek(i).getName(),
                                    daySchedule(lessons, week, getTimestamp(calendar)));
                        }
                        calendar.roll(Calendar.DAY_OF_YEAR, 1);
                    }
                    calendar.roll(Calendar.DAY_OF_YEAR, -7 + indexDayOfWeek);
                }
                else if (command.toLowerCase().equals(OneStepBotCommands.NEXT_WEEK__GROUP_NUMBER.getTitle())) {
                    week += 1;
                    int indexDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                    calendar.roll(Calendar.DAY_OF_YEAR, -indexDayOfWeek + 7);
                    for (int i = 0; i < 7; i++) {
                        if (!schedule.get(groupNumber).getDays().getDayOfWeek(i).getLessons().isEmpty()) {
                            ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays().getDayOfWeek(i).getLessons();
                            text = String.format("%s%s\n%s\n",
                                    text, schedule.get(groupNumber).getDays().getDayOfWeek(i).getName(),
                                    daySchedule(lessons, week, getTimestamp(calendar)));
                        }
                        calendar.roll(Calendar.DAY_OF_YEAR, 1);
                    }
                    calendar.roll(Calendar.DAY_OF_YEAR, -14 + indexDayOfWeek);
                }
                else if (command.toLowerCase().equals(OneStepBotCommands.GROUP.getTitle())) {
                    userLog.setUserId(update.getMessage().getFrom().getId().toString());
                    userLog.setCommand(groupNumber);
                    usersLog.addUserLog(userLog);
                    usersLog.newLog();
                    text = "Ваша группа успешно сохранена";
                }
                else if (command.toLowerCase().equals(OneStepBotCommands.NEAR_LESSON__GROUP_NUMBER.getTitle())) {
                    int secondsOfDay = getSecondsOfDay(calendar);
                    int today = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                    ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays().getDayOfWeek(today).getLessons();
                    int numberOfRolls = 0;
                    while (text.isEmpty()) {
                        for (Lesson lesson : lessons) {
                            if (secondsOfDay <= lesson.getEndTimeSeconds() || numberOfRolls >= 1) {
                                text = lesson.toString(getTimestamp(calendar));
                                break;
                            }
                        }
                        calendar.roll(Calendar.DAY_OF_YEAR, 1);
                        numberOfRolls += 1;
                    }
                    calendar.roll(Calendar.DAY_OF_YEAR, -numberOfRolls);
                }
                else {
                    text = "Мы не знаем такой команды";
                }
            }
            else {
                text = "Вы ввели некорректный номер группы";
            }
        }
        else {
            text = "Мы не знаем такой команды";
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    static long getTimestamp(Calendar cl) {
        return cl.getTimeInMillis() / 1000;
    }

    static int getSecondsOfDay(Calendar cl) {
        return (cl.get(Calendar.HOUR_OF_DAY) * 60 + cl.get(Calendar.MINUTE)) * 60 + cl.get(Calendar.SECOND);
    }

    SendMessage TwoStepAnswers(Update update, BotCommands botCommands, UsersLog usersLog,
                               HashMap<String, Group> schedule) throws IOException {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        if (text.startsWith("М")) {
            if (usersLog.contains(update)) {
                String commandPiece;
                String group = usersLog.getGroup(update.getMessage().getFrom().getId().toString());
                commandPiece = switch (text) {
                    case "Мое расписание на сегодня" -> "Расписание на сегодня";
                    case "Мое расписание на завтра" -> "Расписание на завтра";
                    case "Мое расписание на неделю" -> "Расписание на неделю";
                    case "Мое расписание на следующую неделю" -> "Расписание на следующую неделю";
                    default -> "";
                };
                sendMessage = OneStepAnswers(update, usersLog,botCommands, commandPiece, group, schedule );
            }
            else {
                sendMessage.setText("Введите Группа [группа] \nИ повторите попытку");
            }
        }
        else if (botCommands.oneStepCommand.contains(update.getMessage().getText())){
            sendMessage.setText("Введите номер вашей группы");
            UsersLog.UserLog userLog = new UsersLog.UserLog();
            userLog.setCommand(update.getMessage().getText());
            userLog.setUserId(update.getMessage().getFrom().getId().toString());
            usersLog.addUserLog(userLog);
            usersLog.newLog();
        }

        sendMessage.setChatId(chatId);
        return sendMessage;
    }
}
