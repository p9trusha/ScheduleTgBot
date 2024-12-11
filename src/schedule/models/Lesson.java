package schedule.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lesson {
    private String teacher;
    private int week;
    private String name;
    private String room;
    private String url;

    @SerializedName("second_teacher")
    private String secondTeacher;
    @SerializedName("subject_type")
    private String subjectType;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("start_time_seconds")
    private int startTimeSeconds;
    @SerializedName("end_time_seconds")
    private int endTimeSeconds;
    @SerializedName("is_distant")
    private boolean isDistant;
    @SerializedName("temp_changes")
    private ArrayList<TempChange> tempChanges;

    public String getTeacher() {
        return teacher;
    }

    public int getWeek() {
        return week;
    }

    public String getName() {
        return name;
    }

    public ArrayList<TempChange> getTempChanges() {
        return tempChanges;
    }

    public boolean isDistant() {
        return isDistant;
    }

    public int getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public int getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public String getSecondTeacher() {
        return secondTeacher;
    }

    public String getUrl() {
        return url;
    }

    public String getRoom() {
        return room;
    }
}
