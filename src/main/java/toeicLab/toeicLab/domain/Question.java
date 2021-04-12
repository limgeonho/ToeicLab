package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String image;

    private String questionExplanation;

    private int smallSetType;

    private int smallSetId;

    private String answer;

}
