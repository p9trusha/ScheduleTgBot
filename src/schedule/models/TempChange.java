package schedule.models;

import com.google.gson.annotations.SerializedName;


public class TempChange {
    private String type;
    private String teacher;
    private String room;

    @SerializedName("start_time")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("start_timestamp")
    private long startTimestamp;

    @SerializedName("end_timestamp")
    private long endTimestamp;

    public String getType() {
        return type;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoom() {
        return room;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }
}
