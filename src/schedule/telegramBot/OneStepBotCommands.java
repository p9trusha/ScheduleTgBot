package schedule.telegramBot;

public enum OneStepBotCommands {
    START("/start"),
    INFO("/info"),
    TODAY__GROUP_NUMBER("расписание на сегодня"),
    TOMORROW__GROUP_NUMBER("расписание на завтра"),
    THIS_WEEK__GROUP_NUMBER("расписание на неделю"),
    NEXT_WEEK__GROUP_NUMBER("расписание на следующую неделю"),
    GROUP("группа"),
    NEAR_LESSON__GROUP_NUMBER("ближайшее занятие");

    private final String title;

    OneStepBotCommands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}