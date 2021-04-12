package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("LC")
public class LC extends Question {
    private String recording;

    private String content;

    private String exampleA;

    private String exampleB;

    private String exampleC;

    private String exampleD;

    private String solution;

}
