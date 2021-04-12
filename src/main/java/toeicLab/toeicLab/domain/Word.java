package toeicLab.toeicLab.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    Member member;

    @ElementCollection
    private Map<String, String> word = new HashMap<>();

//////////////////////////////////////////////////
//    private String example;
//
//    private String translationExample;
///////////////////////////////////////////////////
}
