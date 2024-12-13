package schedule.telegramBot;
import org.apache.commons.lang3.StringUtils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import schedule.models.Group;
import schedule.models.Lesson;
import java.io.IOException;
import java.util.*;


public class BotAnswers {
    private final HashMap<String, Group> schedule;
    public BotAnswers(HashMap<String, Group> schedule) {
        this.schedule = schedule;
    }
    public boolean isGroup(String s, HashMap<String, Group> schedule) {
        return schedule.containsKey(s);
    }

    public ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRows());
        return replyKeyboardMarkup;
    }


    public List<KeyboardRow> keyboardRows() {
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtonslevel1()));
        rows.add(new KeyboardRow(keyboardButtonslevel2()));
        rows.add(new KeyboardRow(keyboardButtonslevel3()));
        return rows;
    }
    public List<KeyboardButton> keyboardButtonslevel1() {
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton("Мое расписание на сегодня"));
        buttons.add(new KeyboardButton("Мое расписание на завтра"));
        buttons.add(new KeyboardButton("Мое расписание на неделю"));
        return buttons;
    }
    public List<KeyboardButton> keyboardButtonslevel2() {
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton("Мое расписание на неделю"));
        buttons.add(new KeyboardButton("мое расписание на следующую неделю"));
        return buttons;
    }
    public List<KeyboardButton> keyboardButtonslevel3() {
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton("Мое ближайшее занятие"));
        return buttons;
    }
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

    /**
     *
     * @param update is users data from telegram
     * @param usersLog is users logs
     * @param botCommands commands of bot
     * @param command is command from user
     * @param groupNumber is group of user
     * @return text
     */
    String OneStepAnswers(Update update, UsersLog usersLog, BotCommands botCommands, String command,
                               String groupNumber) throws IOException {
        String text = "";
        Calendar calendar = new GregorianCalendar();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        week -= switch (calendar.get(Calendar.MONTH) < 9) {
            case false -> new GregorianCalendar(calendar.get(Calendar.YEAR),
                    Calendar.SEPTEMBER,
                    1).get(Calendar.WEEK_OF_YEAR);
            case true -> new GregorianCalendar(calendar.get(Calendar.YEAR) - 1,
                    Calendar.SEPTEMBER,
                    1).get(Calendar.WEEK_OF_YEAR);
        };
        UsersLog.UserLog userLog = new UsersLog.UserLog();
        switch (OneStepBotCommands.INFO.whichCommand(command)) {
            case 0 -> text = String.valueOf(startCommand(botCommands));
            case 1 -> text = infoCommand();
            case 2 -> {
                int today = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays().getDayOfWeek(today).getLessons();
                text = String.format("Расписание на %d.%d\n%s",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        daySchedule(lessons, week, getTimestamp(calendar)));
            }
            case 3 -> {
                calendar.roll(Calendar.DAY_OF_YEAR, 1);
                int tomorrow = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                if (tomorrow == 0) {
                    week++;
                }
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
            case 4 -> {
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
            case 5 -> {
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
            case 6 -> {
                usersLog.deleteGroup(update.getMessage().getFrom().getId().toString());
                userLog.setUserId(update.getMessage().getFrom().getId().toString());
                userLog.setCommand(groupNumber);
                usersLog.addUserLog(userLog);
                usersLog.newLog();
                text = "Ваша группа успешно сохранена";

            }
            case 7 -> {
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
            default -> text = "Данная команда не обрабатывается в программе";
        }
        return text;
    }

    static long getTimestamp(Calendar cl) {
        return cl.getTimeInMillis() / 1000;
    }

    static int getSecondsOfDay(Calendar cl) {
        return (cl.get(Calendar.HOUR_OF_DAY) * 60 + cl.get(Calendar.MINUTE)) * 60 + cl.get(Calendar.SECOND);
    }

    String TwoStepAnswers(Update update, BotCommands botCommands, UsersLog usersLog) throws IOException {
        String message = update.getMessage().getText().toLowerCase();
        String text;
        if (message.startsWith("мое")) {
            if (usersLog.contains(update)) {
                String command = message.replaceFirst("мое ", "");
                String group = usersLog.getGroup(update.getMessage().getFrom().getId().toString());
                text = OneStepAnswers(update, usersLog,botCommands, command, group );
            }
            else {
                text = ("Введите Группа [группа] \nИ повторите попытку");
            }
        }
        else if (botCommands.oneStepCommand.contains(update.getMessage().getText())){
            text = ("Введите номер вашей группы");
            UsersLog.UserLog userLog = new UsersLog.UserLog();
            userLog.setCommand(update.getMessage().getText());
            userLog.setUserId(update.getMessage().getFrom().getId().toString());
            usersLog.addUserLog(userLog);
            usersLog.newLog();
        } else {
            text = "Команда не обрабатывается";
        }
        return text;
    }
}
