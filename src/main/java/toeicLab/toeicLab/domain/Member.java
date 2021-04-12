package toeicLab.toeicLab.domain;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private String email;

//    private String emailCheckToken;

    private String contact;

    private int age;

    private LocalDateTime joinedAt;

    @Embedded
    private Address address;

    private String username;

    private String provider;

    private String providerId;

    private String role;

    @Enumerated(EnumType.STRING)
    private LevelType levelType;

    @OneToMany(mappedBy = "member")
    private List<QuestionSet> questionSetList = new ArrayList<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER )
    private List<StudyGroup> studyGroupList = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private StudyGroupApplication studyGroupApplication;

    @OneToOne(mappedBy = "member")
    private Word wordList;

    @OneToOne(mappedBy = "member")
    private ReviewNote reviewNoteList;

    @OneToMany
    private List<Schedule> schedules = new ArrayList<>();



    @Transactional
    public void encodePassword(PasswordEncoder passwordEncoder){
        password = passwordEncoder.encode(password);
    }

}
