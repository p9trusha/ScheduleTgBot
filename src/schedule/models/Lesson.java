package schedule.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lesson {
    private String teacher;
    private String subjectType;
    private int week;
    private String name;
    private String room;
    private String form;
    private String url;

    @SerializedName("second_teacher")
    private String secondTeacher;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("start_time_seconds")
    private int startTimeSeconds;
    @SerializedName("end_time_seconds")
    private int endTimeSeconds;
    @SerializedName("temp_changes")
    private ArrayList<TempChange> tempChanges;

    public String getTeacher() {
        return teacher;
    }

    public String getSecondTeacher() {
        return secondTeacher;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public int getWeek() {
        return week;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public int getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public String getRoom() {
        return room;
    }

    public String getForm() {
        return form;
    }

    public ArrayList<TempChange> getTempChanges() {
        return tempChanges;
    }

    public String getUrl() {
        return url;
    }

    public String toString(long dayTimestamp) {
        StringBuilder text = new StringBuilder();
        String teacher = this.teacher;
        String room = this.room;
        for (TempChange change : tempChanges) {
            if (change.getStartTimestamp() <= dayTimestamp && dayTimestamp <= change.getEndTimestamp()) {
                if (change.getType().equals("teacher")) {
                    teacher = change.getTeacher();
                }
                else if (change.getType().equals("room")) {
                    room = change.getRoom();
                }
            }
        }
        text.append(String.format("\n%s-%s\n%s. %s\n",
                startTime, endTime,
                subjectType, name));
        if (!room.isEmpty()) {
            text.append(String.format("ауд. %s\n", room));
        }
        if (!teacher.isEmpty()) {
            text.append(teacher);
            if (!secondTeacher.isEmpty()) {
                text.append(String.format(", %s", secondTeacher));
            }
            text.append("\n");
        }
        if (form.equals("distant")) {
            text.append(String.format("%s\n", url));
        }
        return text.toString();
    }
}
