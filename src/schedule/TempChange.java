package schedule;

import com.google.gson.annotations.SerializedName;

class TempChange {
    String type;
    String teacher;
    String room;

    @SerializedName("start_time")
    String startDate;

    @SerializedName("end_date")
    String endDate;

    @SerializedName("start_timestamp")
    int startTimestamp;

    @SerializedName("end_timestamp")
    int endTimestamp;
}
