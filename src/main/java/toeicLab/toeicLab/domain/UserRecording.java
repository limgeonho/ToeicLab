package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecording {

    @Id @GeneratedValue
    private Long id;

    private String recording;

    @ManyToOne
    private QuestionSet questionSet;

    private String text;

    //////////////////////////////
//    Member member

}
