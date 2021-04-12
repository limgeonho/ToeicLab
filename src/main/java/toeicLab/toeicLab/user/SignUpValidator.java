package toeicLab.toeicLab.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import toeicLab.toeicLab.repository.MemberRepository;

@Component
public class SignUpValidator implements Validator {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    /*
    SignUpForm dto의 유효성을 검사
    (기본) - SignUpForm 에 선언해둔 Annotation 의 내용
            @NotBlack, @Length, @Pattern 등

    (추가) - validate() 메서드 실행
            (보통 DB를 거쳐야 하는 검사인 경우)
     */
    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)target;

        if(memberRepository.existsByUserId(signUpForm.getUserId())) {
            errors.rejectValue(
                    "userId",  // 문제가 있는 필드의 이름
                    "duplicated.userId", // 에러 코드 : 내 맘대로
                    new Object[]{signUpForm.getUserId()}, // MessageFormat 에 바인딩할 에러메시지들
                    "이미 사용중인 아이디입니다." // 기본 메시지
            );
        }

        if(!((SignUpForm) target).getPassword().equals(((SignUpForm) target).getCheck_password())){
            errors.rejectValue(
                    "password",
                    "notsame.password",
                    new Object[]{signUpForm.getPassword()},
                    "두 비밀번호가 일치하지 않습니다."
            );
        }

        if(memberRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue(
                    "email",  // 문제가 있는 필드의 이름
                    "duplicated.email", // 에러 코드 : 내 맘대로
                    new Object[]{signUpForm.getEmail()}, // MessageFormat 에 바인딩할 에러메시지들
                    "이미 사용중인 이메일입니다." // 기본 메시지
            );
        }

        if(memberRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue(
                    "nickname",  // 문제가 있는 필드의 이름
                    "duplicated.nickname", // 에러 코드 : 내 맘대로
                    new Object[]{signUpForm.getNickname()}, // MessageFormat 에 바인딩할 에러메시지들
                    "이미 사용중인 닉네임입니다." // 기본 메시지
            );
        }

        if(memberRepository.existsByContact(signUpForm.getContact())) {
            errors.rejectValue(
                    "contact",  // 문제가 있는 필드의 이름
                    "duplicated.contact", // 에러 코드 : 내 맘대로
                    new Object[]{signUpForm.getContact()}, // MessageFormat 에 바인딩할 에러메시지들
                    "이미 사용중인 전화번호입니다." // 기본 메시지
            );
        }
    }
}
