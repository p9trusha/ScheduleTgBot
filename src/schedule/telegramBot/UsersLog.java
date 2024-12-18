package schedule.telegramBot;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 */
public class UsersLog {
    private ArrayList<UserLog> logs = new ArrayList<>();


    void addUserLog(UserLog userLog) {
        logs.add(userLog);
    }
    ArrayList<UserLog> getLogs() {
        return logs;
    }

    /**
     * @param id is UserId in telegram
     */
    void deleteCommand(String id) {
        logs.removeIf(log -> Objects.equals(log.userId, id)
                && !StringUtils.isNumeric(log.command));
    }
    void deleteGroup(String id) {
        logs.removeIf(log-> Objects.equals(log.userId, id)
                && StringUtils.isNumeric(log.command.substring(0, 4)));
    }

    /**
     * @return command from
     *  logs
     * or null if
     *  logs does not have
     */
    String getCommand(String id) {
        for (UserLog log : logs) {
            if (Objects.equals(id, log.userId)) {
                if ((log.command.length() !=4) && !StringUtils.isNumeric(log.command) ) {
                    return log.command;}
            }
        }
        return null;
    }
    String getGroup(String id) {
        for (UserLog log : logs) {
            if (Objects.equals(id, log.userId)) {
                if (StringUtils.isNumeric(log.command )) {
                    return log.command;
                }
            }
        }
        return null;
    }


    /**
     * @param update is telegramBots class
     * @return true or false
     */
    boolean contains(Update update) {
        if (!logs.isEmpty()) {
            for (UserLog log : logs) {
                if (update.getMessage().getFrom().getId().toString().equals(log.userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     */
    static class UserLog {
        private String userId;
        private String command;

        public String getCommand() {
            return command;
        }
        public String getUserId() {
            return userId;
        }
        public  void setUserId(String s) {
            this.userId = s;
        }
        public  void  setCommand(String s) {
            this.command = s;
        }

    }

    private static final String path = "src/schedule/telegramBot/logUsers.txt";
    public void newLog() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        int t = 0;
        for (UsersLog.UserLog log : this.getLogs()) {
            if (t == 0) {
                t = 1;
                bufferedWriter.write(log.getCommand() + "/" + log.getUserId());
            } else {
                bufferedWriter.newLine();
                bufferedWriter.write(log.getCommand() + "/" + log.getUserId());
            }
        }
        bufferedWriter.close();
    }
    public static void logsReader(UsersLog usersLog) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        String line;
        UsersLog.UserLog userLog = new UsersLog.UserLog();
        while ((line = bufferedReader.readLine()) != null){
            userLog.setCommand(line.substring(0, line.lastIndexOf("/")));
            userLog.setUserId(line.substring(line.lastIndexOf("/")+1));
            usersLog.addUserLog(userLog);
        }
        bufferedReader.close();
    }

}
