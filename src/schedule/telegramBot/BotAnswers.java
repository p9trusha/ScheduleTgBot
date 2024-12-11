package schedule.telegramBot;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import schedule.models.Group;
import schedule.models.Lesson;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class BotAnswers {
    String daySchedule(String group, HashMap<String, Group> schedule, int weekDay, int week) {
        StringBuilder text = new StringBuilder();

        if (StringUtils.isNumeric(group) && (schedule.containsKey(group))) {
            Group.Days days;
            days = schedule.get(group).getDays();
            Group.Days.Day day;
            day = switch (weekDay) {
                case 0 -> days.getMonday();
                case 1 -> days.getTuesday();
                case 2 -> days.getWednesday();
                case 3 -> days.getThursday();
                case 4 -> days.getFriday();
                case 5 -> days.getSaturday();
                default -> days.getSunday();
            };
            int t = week%2 + 1;

            Lesson lesson;
            for (int i = 0; i < day.getLessons().size(); i++) {
                lesson = day.getLessons().get(i);
                if (lesson.getWeek() == t) {
                    text.append(String.format("\n%s %s\n%s-%s\n%s\n",
                            lesson.getName(), lesson.getRoom(),
                            lesson.getStartTime(), lesson.getEndTime(),
                            lesson.getTeacher()));
                }
            }

            System.out.println();
        } else {
            text = new StringBuilder("Вы ввели некорректный номер группы");
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

    SendMessage OneStepAnswers(Update update, BotCommands botCommands, String commandPiece, String endOfMessage, HashMap<String, Group> schedule) {

        String chatId = update.getMessage().getChatId().toString();
        int index = botCommands.oneStepCommand.indexOf(commandPiece);
        String text = "";
        int week;
        Calendar calendar = new GregorianCalendar();

        String[] dayName  = new String[] {"Monday", "Tuesday", "Wednesday", "ThursDay", "Friday", "Saturday"};

        switch (index) {
            case 0:
                text = String.valueOf(startCommand(botCommands));
                break;
            case 1:
                text = infoCommand();
                break;
            case 2:
                calendar.roll(Calendar.DAY_OF_YEAR, 1);
                int tomorrow = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                week = calendar.getWeeksInWeekYear();
                if (tomorrow == 0) {
                    week++;
                }
                text = daySchedule(endOfMessage, schedule, tomorrow, week);

                text = String.format("Расписание на %d.%d\n%s",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        text);
                calendar.roll(Calendar.DAY_OF_YEAR, -1);
                break;
            case 3:
                int today = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
                week = calendar.getWeeksInWeekYear();
                text = daySchedule(endOfMessage, schedule, today, week);
                text = String.format("Расписание на %d.%d\n%s",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        text);
                break;
            case 4:
                week = calendar.getWeeksInWeekYear();
                for (int i = 1; dayName.length >= i; i++) {
                    text = text + dayName[i-1] +'\n';
                    text = text + daySchedule(endOfMessage, schedule, i, week);
                }
                break;
            case 5:
                week = calendar.getWeeksInWeekYear() + 1;
                for (int i = 1; dayName.length >= i; i++) {
                    text = text + dayName[i-1] +'\n';
                    text = text + daySchedule(endOfMessage, schedule, i, week);
                }
                break;
            default:
                text = "Ошибка программы";
        }


        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    SendMessage TwoStepAnswers(Update update, BotCommands botCommands, String commandPiece, String endOfMessage, HashMap<String, Group> schedule) {
        String chatId = update.getMessage().getChatId().toString();
        String text = "Пока не сделано";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    private void execute(SendMessage sendMessage) {
    }
}
