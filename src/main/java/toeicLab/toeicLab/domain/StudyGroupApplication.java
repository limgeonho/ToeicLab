package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupApplication {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private Member member;

    @ElementCollection(targetClass = StudyGroupApplicationTag.class)
    @Enumerated(EnumType.STRING)
    private List<StudyGroupApplicationTag> tags;

    private LocalDateTime submitTime;

    private boolean matching;

    private long value;
}
