package schedule;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Set;

public class Parser {
    static HashMap<String, Group> json(String inputLine) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(inputLine);
        Set<String> groups = jsonObject.keySet();
        HashMap<String, Group> schedule = new HashMap<>();
        for (String group : groups) {
            Group scheduleOfGroup = new Gson().fromJson(jsonObject.get(group), Group.class);
            schedule.put(group, scheduleOfGroup);
        }
        return schedule;
    }
}
