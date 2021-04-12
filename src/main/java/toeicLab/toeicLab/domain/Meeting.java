package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyGroup studyGroup;

    private LocalDateTime date;

    @OneToOne
    private QuestionSet questionSet1;
    @OneToOne
    private QuestionSet questionSet2;
    @OneToOne
    private QuestionSet questionSet3;
    @OneToOne
    private QuestionSet questionSet4;

    private Integer count;

    private LocalDateTime duration;
}
