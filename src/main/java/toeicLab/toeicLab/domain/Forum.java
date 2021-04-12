package toeicLab.toeicLab.domain;

import lombok.*;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Forum {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String userId;

    private LocalDateTime date;

    private Long hit;

    private Long questionId;

    private String nickname;

}
