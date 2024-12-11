package schedule;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Group {
    private String group;
    private Days days;

    public Days getDays() {
        return days;
    }

    public String getGroup() {
        return group;
    }

    static class Days {
        @SerializedName("0")
        private Day monday;
        @SerializedName("1")
        private Day tuesday;
        @SerializedName("2")
        private Day wednesday;
        @SerializedName("3")
        private Day thursday;
        @SerializedName("4")
        private Day friday;
        @SerializedName("5")
        private Day saturday;
        @SerializedName("6")
        private Day sunday;

        public Day getMonday() {
            return monday;
        }

        public Day getTuesday() {
            return tuesday;
        }

        public Day getWednesday() {
            return wednesday;
        }

        public Day getThursday() {
            return thursday;
        }

        public Day getFriday() {
            return friday;
        }

        public Day getSaturday() {
            return saturday;
        }

        public Day getSunday() {
            return sunday;
        }

        static class Day {
            private String name;
            private ArrayList<Lesson> lessons;

            public String getName() {
                return name;
            }

            public ArrayList<Lesson> getLessons() {
                return lessons;
            }
        }
    }
}
