package schedule.telegramBot;

public enum OneStepBotCommands {
    START("/start"),
    INFO("/info"),
    TODAY_GROUP_NUMBER("расписание на сегодня"),
    TOMORROW_GROUP_NUMBER("расписание на завтра"),
    THIS_WEEK_GROUP_NUMBER("расписание на неделю"),
    NEXT_WEEK_GROUP_NUMBER("расписание на следующую неделю"),
    GROUP("группа");

    private final String title;

    OneStepBotCommands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}