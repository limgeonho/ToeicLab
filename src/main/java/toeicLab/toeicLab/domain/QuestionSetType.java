package toeicLab.toeicLab.domain;

public enum QuestionSetType {

    QUARTER_TOEIC(0),
    HALF_TOEIC(1),
    FULL_TOEIC(2),
    HALF_SPEAKING(3),
    FULL_SPEAKING(4),
    MEETING(5),
    LEVEL_TEST(6),
    PRACTICE(7);

    int primary;

    QuestionSetType(int p){
        primary = p;
    }

    public int get() {
        return primary;
    }
}
