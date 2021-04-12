package toeicLab.toeicLab.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import toeicLab.toeicLab.domain.StudyGroupApplicationTag;
import toeicLab.toeicLab.service.StudyGroupApplicationService;

@Component
@RequiredArgsConstructor
public class StudyGroupApplicationValidator implements Validator {

    private final StudyGroupApplicationService studyGroupApplicationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyGroupApplicationForm studyGroupApplicationForm = (StudyGroupApplicationForm) target;
        int value = 1;
        StudyGroupApplicationTag[] tags = studyGroupApplicationForm.getTags();
        for (int i = 0; i < tags.length; i++) {
            value = value * tags[i].get();
        }

        if (studyGroupApplicationForm.getAgreement() == null) {
            errors.rejectValue(
                    "agreement",  // 문제가 있는 필드의 이름
                    "must agree", // 에러 코드 : 내 맘대로
                    new Object[]{studyGroupApplicationForm.getAgreement()}, // MessageFormat 에 바인딩할 에러메시지들
                    "이용약관에 동의하셔야 합니다." // 기본 메시지
            );
        }

        if (value % 2 != 0 && value % 3 != 0 && value % 5 != 0) {
            errors.rejectValue(
                    "tags",  // 문제가 있는 필드의 이름
                    "age tag not selected", // 에러 코드 : 내 맘대로
                    new Object[]{studyGroupApplicationForm.getAgreement()}, // MessageFormat 에 바인딩할 에러메시지들
                    "희망연령을 선택하셔야 합니다." // 기본 메시지
            );
        }

        if (value % 7 != 0 && value % 11 != 0 && value % 13 != 0) {
            errors.rejectValue(
                    "tags",  // 문제가 있는 필드의 이름
                    "level tag not selected", // 에러 코드 : 내 맘대로
                    new Object[]{studyGroupApplicationForm.getAgreement()}, // MessageFormat 에 바인딩할 에러메시지들
                    "희망레벨을 선택하셔야 합니다." // 기본 메시지
            );
        }

        if (value % 17 != 0 && value % 19 != 0) {
            errors.rejectValue(
                    "tags",  // 문제가 있는 필드의 이름
                    "day tag not selected", // 에러 코드 : 내 맘대로
                    new Object[]{studyGroupApplicationForm.getAgreement()}, // MessageFormat 에 바인딩할 에러메시지들
                    "희망 날짜를 선택하셔야 합니다." // 기본 메시지
            );
        }

        if (value % 23 != 0 && value % 29 != 0) {
            errors.rejectValue(
                    "tags",  // 문제가 있는 필드의 이름
                    "gender tag not selected", // 에러 코드 : 내 맘대로
                    new Object[]{studyGroupApplicationForm.getAgreement()}, // MessageFormat 에 바인딩할 에러메시지들
                    "희망 성별을 선택하셔야 합니다." // 기본 메시지
            );
        }

    }
}
