package toeicLab.toeicLab.domain;

public enum GenderType {
    MALE(23),FEMALE(29);

    int primary;

    GenderType(int p){
        primary = p;
    }


    public int get() {
        return primary;
    }
}
