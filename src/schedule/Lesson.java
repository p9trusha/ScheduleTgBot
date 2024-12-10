package schedule;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;


public class Lesson {
    public String teacher;
    public int week;
    public String name;
    public String room;
    public String url;


    @SerializedName("second_teacher") String secondTeacher;
    @SerializedName("subject_type") String subjectType;
    public @SerializedName("start_time") String startTime;
    public @SerializedName("end_time") String endTime;
    @SerializedName("start_time_seconds") int startTimeSeconds;
    @SerializedName("end_time_seconds") int endTimeSeconds;
    @SerializedName("is_distant") boolean isDistant;
    @SerializedName("temp_changes") ArrayList<TempChange> tempChanges;
}
