package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDto {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String emailCheckToken;

    public String generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString();
        return emailCheckToken;
    }
}
