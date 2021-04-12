package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSet{

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Member member;

    @Enumerated
    private QuestionSetType questionSetType;

    private LocalDateTime createdAt;

    @ManyToMany
    private List<Question> questions = new ArrayList<>();

    @ElementCollection
    private Map<Long, String> submittedAnswers = new HashMap<>();

    @OneToMany(mappedBy = "questionSet")
    private List<UserRecording> userRecordings = new ArrayList<>();


    private LocalDateTime timer;




}
