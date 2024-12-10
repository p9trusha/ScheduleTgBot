package schedule;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Lesson {
    String teacher;
    int week;
    String name;
    String room;
    String url;

    @SerializedName("second_teacher") String secondTeacher;
    @SerializedName("subject_type") String subjectType;
    @SerializedName("start_time") String startTime;
    @SerializedName("end_time") String endTime;
    @SerializedName("start_time_seconds") int startTimeSeconds;
    @SerializedName("end_time_seconds") int endTimeSeconds;
    @SerializedName("is_distant") boolean isDistant;
    @SerializedName("temp_changes") ArrayList<TempChange> tempChanges;
}
