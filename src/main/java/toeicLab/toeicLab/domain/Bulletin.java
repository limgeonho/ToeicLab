package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bulletin {

        @Id @GeneratedValue
        private Long id;

        private String title;

        private String content;

        private LocalDateTime date;

        private String writerId;

        private String nickname;

        @ElementCollection
        private Set<String> likeSets = new HashSet<>();

        // 조회수
        private Long hit;

        // 좋아요
        private Long likeNumber;

    }
