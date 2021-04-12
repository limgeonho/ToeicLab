package toeicLab.toeicLab.user;

import lombok.Data;
import toeicLab.toeicLab.domain.StudyGroupApplicationTag;

@Data
public class StudyGroupApplicationForm {

    private String agreement;

    private StudyGroupApplicationTag[] tags;

}
