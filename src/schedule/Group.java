package schedule;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;


public class Group {
    String group;

    public Days days;


    public static class Days {
        public @SerializedName("0") Day monday;
        public @SerializedName("1") Day tuesday;
        public @SerializedName("2") Day wednesday;
        public @SerializedName("3") Day thursday;
        public @SerializedName("4") Day friday;
        public @SerializedName("5") Day saturday;
        public @SerializedName("6") Day sunday;

        public static class Day {
            String name;
            public ArrayList<Lesson> lessons;

        }
    }


}
