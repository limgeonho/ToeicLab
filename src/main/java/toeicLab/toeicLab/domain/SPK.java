package toeicLab.toeicLab.domain;

import lombok.*;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorValue("SPK")
public class SPK extends Question{

    private String recording;

    @ElementCollection
    private List<String> keyword = new ArrayList<>();
}
