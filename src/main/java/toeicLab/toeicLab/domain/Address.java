package toeicLab.toeicLab.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address{
    private String city;
    private String street;
    private String zipcode;
}
