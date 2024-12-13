package schedule.telegramBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public int whichCommand(String s) {
        s = s.toLowerCase();
        if (s.equals(START.getTitle())) return 0;
        if (s.equals(INFO.getTitle())) return 1;
        if (s.equals(TODAY__GROUP_NUMBER.getTitle())) return 2;
        if (s.equals(TOMORROW__GROUP_NUMBER.getTitle())) return 3;
        if (s.equals(THIS_WEEK__GROUP_NUMBER.getTitle())) return 4;
        if (s.equals(NEXT_WEEK__GROUP_NUMBER.getTitle())) return 5;
        if (s.equals(GROUP.getTitle())) return 6;
        if (s.equals(NEAR_LESSON__GROUP_NUMBER.getTitle())) return 7;
        return -1;

    }

}