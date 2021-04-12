package toeicLab.toeicLab.domain;

public enum LevelType {
    ADVANCED(7),INTERMEDIATE(11),BEGINNER(13);

    int primary;

    LevelType(int p){
        primary = p;
    }

    public int get() {
        return primary;
    }
}
