package toeicLab.toeicLab.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateForm {

    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String gender;

    @NotBlank
    private Integer age;

    @NotBlank
    private String contact;

    @NotBlank
    private String zipcode;
    private String city;
    private String street;

}
