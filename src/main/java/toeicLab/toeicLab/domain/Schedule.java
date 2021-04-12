package toeicLab.toeicLab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Schedule {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime time;

    @OneToOne
    private QuestionSet questionSet;
}
