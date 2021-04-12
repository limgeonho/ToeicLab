package toeicLab.toeicLab.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignUpForm {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String check_password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String gender;

    @NotNull
    private int age;

    @NotBlank
    @Length(min = 5, max=40)
    @Email
    private String email;

    @NotBlank
    private String contact;

    @NotBlank
    private String zipcode;
    private String city;
    private String street;

}
