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

    private static final String START_COMMAND = "/start";
    private static final String INFO_COMMAND = "/info";
    private static final String TODAY__GROUP_NUMBER = "расписание на сегодня";
    private static final String TOMORROW__GROUP_NUMBER = "расписание на завтра";
    private static final String THIS_WEEK__GROUP_NUMBER = "расписание на неделю";
    private static final String NEXT_WEEK__GROUP_NUMBER = "расписание на следующую неделю";
    private static final String GROUP ="группа";
    private static final String NEAR_LESSON__GROUP_NUMBER = "ближайшее занятие";


    private final HashMap<String, Group> schedule;
    public BotAnswers(HashMap<String, Group> schedule) {
        this.schedule = schedule;
    }
    public boolean isGroup(String s, HashMap<String, Group> schedule) {
        return schedule.containsKey(s);
    }
    public ReplyKeyboardMarkup replyKeyboardMarkup(ArrayList<String> array) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRows(array));
        return replyKeyboardMarkup;
    }
    public List<KeyboardRow> keyboardRows(ArrayList<String> array) {
        List<KeyboardRow> rows = new ArrayList<>();
        List<List<KeyboardButton>> buttonsLevels = keyboardButtonsLevels(array);
        for (List<KeyboardButton> keyboardButtons : buttonsLevels) {
            rows.add(new KeyboardRow(keyboardButtons));
        }
        return rows;
    }
    public List<List<KeyboardButton>> keyboardButtonsLevels(ArrayList<String> array) {
        List<List<KeyboardButton>> buttonsLevels = new ArrayList<>();
        List<KeyboardButton> buttons = new ArrayList<>();
        for (int i = 0; i<array.size(); i++) {
            if (i%2==0) {
                buttonsLevels.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(new KeyboardButton(array.get(i)));
        }
        buttonsLevels.add(buttons);
        return buttonsLevels;
    }
    String daySchedule(ArrayList<Lesson> lessons, int week, long dayTimestamp) {
        String text = "";
        int weekParity;
        if (week % 2 == 0) {
            weekParity = 2;
        }
        else {
            weekParity = 1;
        }
        for (Lesson lesson : lessons) {
            if (lesson.getWeek() == weekParity) {
                text = text.concat(lesson.toString(dayTimestamp));
            }
        }
    return text;
    }

    String startCommand(BotCommands botCommands) {
        String text = "Приветствуем в нашем боте, доступны такие команды:\nОдноступенчатые\n";
        for (int i = 0; i < botCommands.oneStepCommand.size(); i++) {
            if (botCommands.oneStepCommand.get(i).startsWith("/")) {
                text = text.concat(botCommands.oneStepCommand.get(i) +'\n');
            }
            else {
                text = text.concat(botCommands.oneStepCommand.get(i) + " [Номер группы]\n");
            }
        }
        text = text.concat("Двухступенчатые:\n");
        for (int i = 0; i < botCommands.twoStepCommand.size(); i++) {
            text = text.concat(botCommands.twoStepCommand.get(i) + '\n');
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
     * OneStepAnswers register a full command only. Example (/start or schedule to tomorrow 3354
     * @param update is users data from telegram
     * @param usersLog users logs (like, Vasya is from **** this group and he can write fast commands
     *                       or Vasya wrote schedule to tomorrow without group number, and usersLog save this move)
     *
     * @param botCommands commands of bot
     * @param command is command from user
     * @param groupNumber is group of user
     * @return String text answers
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

        switch (command) {
            case START_COMMAND -> text = startCommand(botCommands);
            case INFO_COMMAND -> text = infoCommand();
            case TODAY__GROUP_NUMBER -> {
                int today = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                ArrayList<Lesson> lessons = schedule.get(groupNumber).getDays().getDayOfWeek(today).getLessons();
                text = String.format("Расписание на %d.%d\n%s",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        daySchedule(lessons, week, getTimestamp(calendar)));
            }
            case TOMORROW__GROUP_NUMBER -> {
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
            case THIS_WEEK__GROUP_NUMBER -> {
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
            case NEXT_WEEK__GROUP_NUMBER -> {
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
            case GROUP -> {
                usersLog.deleteGroup(update.getMessage().getFrom().getId().toString());
                userLog.setUserId(update.getMessage().getFrom().getId().toString());
                userLog.setCommand(groupNumber);
                usersLog.addUserLog(userLog);
                usersLog.newLog();
                text = "Ваша группа успешно сохранена";

            }
            case NEAR_LESSON__GROUP_NUMBER -> {
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

    /**
     * TwoStepAnswers have a fast commands without group name, and he register command without group
     * @param update is users data
     * @param botCommands is commands
     * @param usersLog users logs (like, Vasya is from **** this group and he can write fast commands
     *                 or Vasya wrote schedule to tomorrow without group number, and usersLog save this move)
     * @return String text answers
     * @throws IOException
     */
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
