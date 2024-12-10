package schedule;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Group {
    String group;
    Days days;

    static class Days {
        @SerializedName("0") Day monday;
        @SerializedName("1") Day tuesday;
        @SerializedName("2") Day wednesday;
        @SerializedName("3") Day thursday;
        @SerializedName("4") Day friday;
        @SerializedName("5") Day saturday;
        @SerializedName("6") Day sunday;

        static class Day {
            String name;
            ArrayList<Lesson> lessons;
        }
    }
}
