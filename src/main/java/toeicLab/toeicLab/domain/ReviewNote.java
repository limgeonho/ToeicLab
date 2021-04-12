package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNote {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private Member member;

    @OneToMany
    private List<Question> questions = new ArrayList<>();

    @ElementCollection
    private Map<Long, String> submittedAnswers = new HashMap<>();

}
