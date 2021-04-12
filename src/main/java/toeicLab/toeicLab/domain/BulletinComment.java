package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulletinComment {
    @Id
    @GeneratedValue
    private Long id;

    private Long bulletinId;

    private String commentWriter;

    private LocalDateTime date;

    private String comment;
}
