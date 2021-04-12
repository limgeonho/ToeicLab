package toeicLab.toeicLab.domain;

public enum StudyGroupApplicationTag {
    AGE_10S(2), AGE_20S(3), AGE_30S(5),
    LEVEL_BEGINNER(7), LEVEL_INTERMEDIATE(11), LEVEL_ADVANCED(13),
    WEEKDAY(17), WEEKEND(19),
    MALE(23), FEMALE(29),
    NONE(1); // 임시로 1, 모든 값들의 곱

    int primary;

    StudyGroupApplicationTag(int p){
        primary = p;
    }

    public int get() {
        return primary;
    }

    
}
