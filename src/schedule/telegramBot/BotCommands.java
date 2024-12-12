package schedule.telegramBot;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class BotCommands {
    /**commands like schedule tomorrow 3354
     * schedule
     */
    public ArrayList<String> oneStepCommand = new ArrayList<>();
    /**
     * commands like schedule tomorrow
     * group?
     * 3354
     * schedule
     */
    public ArrayList<String> twoStepCommand = new ArrayList<>();
    public void addOneStepCommand(String s) {
        this.oneStepCommand.add(s);
    }
    public void addTwoStepCommand(String s) {
        this.twoStepCommand.add(s);
    }
}

