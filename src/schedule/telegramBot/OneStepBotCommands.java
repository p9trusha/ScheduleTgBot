package schedule.telegramBot;

class OneStepBotCommand {
    private final int id;
    private final String text;

    private OneStepBotCommand(int id, String text) {
        this.id = id;
        this.text = text;
    }

    static OneStepBotCommand start = new OneStepBotCommand(0, "/start");
    static OneStepBotCommand info = new OneStepBotCommand(1, "/info");
    static OneStepBotCommand today = new OneStepBotCommand(2, "расписание на сегодня");
    static OneStepBotCommand tomorrow = new OneStepBotCommand(3, "расписание на завтра");
    static OneStepBotCommand thisWeek = new OneStepBotCommand(4, "расписание на неделю");
    static OneStepBotCommand nextWeek = new OneStepBotCommand(5, "расписание на следующую неделю");
    static OneStepBotCommand nearLesson = new OneStepBotCommand(6, "ближайшее занятие");

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}

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