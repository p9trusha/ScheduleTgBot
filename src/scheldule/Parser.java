package scheldule;

public class Parser {
    void json(String groupNumber, String inputLine) {
        String TagGroupNumber = groupNumber;
        String TagGroup = "group";
        String TagDays = "days";
        String[] TagsNumberOfDay = new String[7];
        for (int i = 0; i < 7; i++) {
            TagsNumberOfDay[i] = String.valueOf(i);
        }
        String TagLessons = "lessons";
        String TagTeacher = "teacher";
        String TagSecondTeacher = "second_teacher";
        String TagSubjectType = "subjectType";
        String TagWeek = "week";
        String TagName = "name";
        String TagStartTime = "start_time";
        String TagEndTime = "end_time";
        String TagRoom = "room";
        String TagIsDistant = "is_distant";
        String TagURL = "url";
    }
}
