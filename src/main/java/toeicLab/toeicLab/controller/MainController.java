package toeicLab.toeicLab.controller;

import com.google.gson.JsonObject;
import com.sun.xml.bind.v2.util.QNameMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.*;
import toeicLab.toeicLab.service.*;
import toeicLab.toeicLab.user.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.http.HttpRequest;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SignUpValidator signUpValidator;
    private final MemberService memberService;
    private final MailService mailService;
    private final MailRepository mailRepository;
    private final MemberRepository memberRepository;
    private final QuestionSetService questionSetService;
    private final QuestionSetRepository questionSetRepository;
    private final StudyGroupApplicationValidator studyGroupApplicationValidator;
    private final StudyGroupApplicationService studyGroupApplicationService;
    private final StudyGroupRepository studyGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/index")
    public String index(@CurrentUser Member member, Model model) {
        if (member != null) {
            model.addAttribute(member);
        }
        return "view/index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "view/login";
    }

    @GetMapping("/logout")
    public String showLogoutPage() {
        return "view/index";
    }

    @GetMapping("/send_reset_password_link")
    public String sendResetPasswordView() {
        return "view/send_reset_password_link";
    }

    @PostMapping("/send_reset_password_link")
    public String sendResetPassword(@RequestParam("userId") String userId, @RequestParam("email") String email, Model model) {
        try {
            Member checkedMember = memberService.sendResetPasswordEmail(userId, email);
            model.addAttribute("checkedMember", checkedMember);
            MailDto mailDto = new MailDto();
            mailDto.setEmail(checkedMember.getEmail());
            mailDto.setEmailCheckToken(mailDto.generateEmailCheckToken());

            mailService.resetPasswordMailSend(mailDto);
            MailDto existedMail = mailRepository.findByEmail(email);
            existedMail.setEmailCheckToken(mailDto.getEmailCheckToken());
            mailRepository.save(existedMail);

            model.addAttribute("resetPasswordEmail", mailDto);
            log.info("이메일보내기 성공");


        } catch (IllegalArgumentException e) {
            log.info("실패");
            model.addAttribute("error_code", "password.reset.failed");
            return "view/notify_password";
        }
        log.info("뷰페이지 보내줘어");
        model.addAttribute("result_code", "password.reset.send");
        log.info("뷰페이지에 result_code 담기성공");
        return "view/notify_password";
    }

    @PostMapping("/notify_password")
    public String goResetPassword(@RequestParam("token") String resetPasswordEmailToken, Model model) {
        MailDto mailByEmailCheckToken = mailRepository.findByEmailCheckToken(resetPasswordEmailToken);
        model.addAttribute("email", mailByEmailCheckToken.getEmail());
        return "view/reset_password";
    }


    @GetMapping("/reset/checkTokens")
    @ResponseBody
    public String resetCheckTokens(@RequestParam("resetPasswordEmail") String resetPasswordEmail,
                                   @RequestParam("token") String resetPasswordEmailToken) {
        log.info("비밀번호 초기화 인증키 판단");
        JsonObject jsonObject = new JsonObject();
        MailDto getResetPasswordTokenMail = mailRepository.findByEmail(resetPasswordEmail);
        log.info(getResetPasswordTokenMail.getEmail());
        boolean result = false;

        result = getResetPasswordTokenMail.getEmailCheckToken().equals(resetPasswordEmailToken);

        if (result) {
            jsonObject.addProperty("message", "비밀번호 초기화 인증 성공");
        } else {
            jsonObject.addProperty("message", "비밀번호 초기화 인증번호가 일치하지 않습니다.");
        }
        return jsonObject.toString();
    }


    @GetMapping("/notify_password")
    public String notifyPasswordView() {
        return "view/notify_password";
    }

    @GetMapping("reset_password")
    public String resetPasswordView() {
        return "view/reset_password";
    }


    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        memberService.resetPassword(email, password);
        log.info("비밀번호 수정 완료");
        model.addAttribute("result_code", "password.reset.success");
        return "view/notify_password";
    }

    @GetMapping("/send_find_id_link")
    public String sendFindIdView() {
        return "view/send_find_id_link";
    }

    @PostMapping("/send_find_id_link")
    public String sendFindId(String email, Model model) {
        try {
            Member findIdMember = memberService.sendFindIdByEmail(email);
            model.addAttribute("findIdMember", findIdMember);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error_code", "find.id.failed");
            return "view/find_id";
        }
        model.addAttribute("success_code", "find.id.success");
        return "view/find_id";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute(new SignUpForm());
        return "/view/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            log.info("유효성 에러 발생!");
            return "view/signup";
        }
        signUpValidator.validate(signUpForm, errors);
        log.info("유효성 검사 끝!");

        Member member = memberService.createNewMember(signUpForm);

        memberService.autologin(member); // 해당 멤버를 자동 로그인 해주기

        return "redirect:/index";
    }

    @GetMapping("/signup/email")
    @ResponseBody
    public String sendEmailCheckToken(@RequestParam("email") String email) {

        JsonObject jsonObject = new JsonObject();
        boolean result = false;

        MailDto checkEmail = mailRepository.findByEmail(email);
        if(checkEmail==null){
            MailDto mailDto = new MailDto();
            mailDto.setEmail(email);
            mailDto.setEmailCheckToken(mailDto.generateEmailCheckToken());
            MailDto saveMail = mailService.mailSend(mailDto);
            mailRepository.save(saveMail);
            result = saveMail != null;
        }else{
            result = false;
        }

        if (result) {
            jsonObject.addProperty("message", "이메일을 확인하세요.");
        } else {
            jsonObject.addProperty("message", "이미 존재하는 이메일입니다.");
        }
        return jsonObject.toString();
    }

    @GetMapping("/signup/checkTokens")
    @ResponseBody
    public String checkTokens(@RequestParam("email") String email, @RequestParam("certification_number") String certification_number) {
        log.info(certification_number);

        JsonObject jsonObject = new JsonObject();
        MailDto getTokenMail = mailRepository.findByEmail(email);
        log.info(getTokenMail.getEmail());
        boolean result = false;

        result = getTokenMail.getEmailCheckToken().equals(certification_number);

        if (result) {
            jsonObject.addProperty("message", "이메일 인증 성공.");
        } else {
            jsonObject.addProperty("message", "이메일 인증번호가 일치하지 않습니다.");
        }
        return jsonObject.toString();
    }

    @GetMapping("/signup/checkUserId")
    @ResponseBody
    public String checkUserId(@RequestParam("userId") String userId) {

        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        result = memberRepository.existsByUserId(userId);

        if (result) {
            jsonObject.addProperty("message", "이미 존재하는 아이디입니다.");
//            jsonObject.addProperty("message",false);
        } else {
            jsonObject.addProperty("message", "사용 가능한 아이디입니다.");
//            jsonObject.addProperty("message",true);

        }
        return jsonObject.toString();
    }

    @GetMapping("/signup/checkNickname")
    @ResponseBody
    public String checkNickname(@RequestParam("nickname") String nickname) {
        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        result = memberRepository.existsByNickname(nickname);

        if (result) {
            jsonObject.addProperty("message", "이미 존재하는 닉네임입니다.");
        } else {
            jsonObject.addProperty("message", "사용 가능한 닉네임입니다.");
        }
        return jsonObject.toString();
    }

    @GetMapping("/signup/checkPasswords")
    @ResponseBody
    public String checkPasswords(@RequestParam("password") String password, @RequestParam("check_password") String check_password) {
        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        result = password.equals(check_password);

        if (result) {
            jsonObject.addProperty("message", "비밀번호가 일치합니다.");
        } else {
            jsonObject.addProperty("message", "비밀번호가 일치하지 않습니다.");
        }
        return jsonObject.toString();
    }

    @GetMapping("/update/password")
    @ResponseBody
    public String updatePassword(@RequestParam("password") String password, @RequestParam("userId") String userId) {

        Member member = memberRepository.findByUserId(userId);
        JsonObject jsonObject = new JsonObject();

        if (member.getPassword() == null) {
            member.setPassword("{noop}" + password);
            member.encodePassword(passwordEncoder);
            memberRepository.save(member);
            jsonObject.addProperty("message", "비밀번호를 등록하였습니다.");
        } else {
            member.setPassword("{noop}" + password);
            memberRepository.save(member);
            jsonObject.addProperty("message", "비밀번호를 수정하였습니다.");
        }
        return jsonObject.toString();
    }

    @GetMapping("/update/delete")
    @ResponseBody
    public String deletePassword(@RequestParam("userId") String userId, HttpServletRequest request) {

        Member deletedMember = memberRepository.findByUserId(userId);
        log.info(String.valueOf(deletedMember.getId()));
        memberRepository.deleteById(deletedMember.getId());

        boolean result = true;
        result = memberRepository.existsById(deletedMember.getId());
        JsonObject jsonObject = new JsonObject();

        if (result) {
            jsonObject.addProperty("message", "[ToeicLab]을 탈퇴실패...");

        } else {
            jsonObject.addProperty("message", "[ToeicLab]을 탈퇴했습니다.");
            HttpSession session= request.getSession(false);
            SecurityContextHolder.clearContext();
            if(session != null) {
                session.invalidate();
            }
        }

        return jsonObject.toString();
    }


    @GetMapping("/my_page")
    public String myPageView(@CurrentUser Member member, Model model) {
        if (member.getGenderType() == null){
            member.setGenderType(GenderType.MALE);
            memberRepository.save(member);
        }
        Member currentUser = memberRepository.findByEmail(member.getEmail());
        model.addAttribute("currentUser", currentUser);
        log.info(String.valueOf(currentUser.getGenderType()));
        model.addAttribute("member", member);
        model.addAttribute(new UpdateForm());
        return "view/my_page";
    }

    @PostMapping("/my_page")
    public String myPageSubmit(UpdateForm updateForm) {
        log.info("회원정보수정 시작");

        Member updatedMember = memberRepository.findByUserId(updateForm.getUserId());
        updatedMember.setAge(updateForm.getAge());
        updatedMember.setContact(updateForm.getContact());
        updatedMember.setAddress(Address.builder()
                .zipcode(updateForm.getZipcode())
                .city(updateForm.getCity())
                .street(updateForm.getStreet())
                .build());
        if (updateForm.getGender().equals("male")) {
            updatedMember.setGenderType(GenderType.MALE);
        } else if (updateForm.getGender().equals("female")) {
            updatedMember.setGenderType(GenderType.FEMALE);
        } else {
            updatedMember.setGenderType(updatedMember.getGenderType());
        }
        memberRepository.save(updatedMember);
        return "redirect:/my_page";
    }

    @GetMapping("/my_studygroup_detail/{id}")
    public String myStudyGroupDetail(@CurrentUser Member member, @PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);
        StudyGroup thisStudyGroup = studyGroupRepository.getOne(longId);
        List<Meeting> meetings = thisStudyGroup.getMeetings();
        List<QuestionSet> questionSets = new ArrayList<>();
        Map<Long, String> comment = new HashMap<>();

        if (meetings != null) {
            for (Meeting meeting : meetings) {
                if (meeting.getQuestionSet1().getMember().getId().equals(member.getId())) {
                    questionSets.add(meeting.getQuestionSet1());
                }
                if (meeting.getQuestionSet2().getMember().getId().equals(member.getId())) {
                    questionSets.add(meeting.getQuestionSet2());
                }
                if (meeting.getQuestionSet3().getMember().getId().equals(member.getId())) {
                    questionSets.add(meeting.getQuestionSet3());
                }
                if (meeting.getQuestionSet4().getMember().getId().equals(member.getId())) {
                    questionSets.add(meeting.getQuestionSet4());
                }
            }
            model.addAttribute("questionSets", questionSets);

            for(QuestionSet qs : questionSets){
                if (!qs.getSubmittedAnswers().isEmpty()){
                    System.out.println(qs.getSubmittedAnswers());
                    comment.put(qs.getId(), memberService.CreateProgressByQuestionSet(qs));
                    model.addAttribute("checkToken", qs.getId());
                }
                else {
                    comment.put(qs.getId(), "스터디를 진행하면 comment 가 생성됩니다");
                }
            }
            model.addAttribute("comment", comment);
        }


        model.addAttribute("member", member);
        model.addAttribute("studyGroupId", Long.parseLong(id));
        model.addAttribute("thisStudyGroup", thisStudyGroup);
        model.addAttribute("member1", thisStudyGroup.getMembers().get(0));
        model.addAttribute("member2", thisStudyGroup.getMembers().get(1));
        model.addAttribute("member3", thisStudyGroup.getMembers().get(2));
        model.addAttribute("member4", thisStudyGroup.getMembers().get(3));
        model.addAttribute("meetings", meetings);
        return "view/my_studygroup_detail";
    }

    @PostMapping("/create_meeting/{id}")
    public String myStudyGroupDetailPost(@CurrentUser Member member, @PathVariable String id, Model model, String date,
                                         String[] select_form) {

        int[] numberOfQuestions = questionSetService.selectFormToArray(select_form);

        StudyGroup thisStudyGroup = studyGroupRepository.findById(Long.parseLong(id));
        model.addAttribute("questionSetId", id);
        model.addAttribute("member", member);
        questionSetService.createMeeting(thisStudyGroup, numberOfQuestions, date);
        return "redirect:/my_studygroup_detail/{id}";
    }

    @GetMapping("/create_meeting/{id}")
    public String viewCreateMeeting(@CurrentUser Member member, Model model, @PathVariable Long id) {
        model.addAttribute("member", member);
        model.addAttribute("studyGroupId", id);
        return "view/create_meeting";
    }

    @GetMapping("/toeiclab_introduction")
    public String toeiclabIntroduction() {
        return "view/toeiclab_intro";
    }

    @GetMapping("/select_test")
    public String selectTest(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/select_test";
    }

    @GetMapping("/rc_sheet")
    public String rcSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/rc_sheet";
    }

    @GetMapping("/lc_sheet")
    public String lcSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "/practice_sheet";
    }

    @GetMapping("/spk_sheet")
    public String spkSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/spk_sheet";
    }

    @GetMapping("/spk_confirm_answer")
    public String spkConfirmAnswer(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/spk_confirm_answer";
    }

    @GetMapping("/apply_studygroup")
    public String applyStudyGroup(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        model.addAttribute(new StudyGroupApplicationForm());
        return "view/apply_studygroup";
    }

    @PostMapping("/apply_studygroup")
    public String SubmitStudyGroupApplication(Model model, @CurrentUser Member member, @Valid StudyGroupApplicationForm studyGroupApplicationForm, Errors errors) {
        model.addAttribute("member", member);
        studyGroupApplicationValidator.validate(studyGroupApplicationForm, errors);
        if (errors.hasErrors()) {
            log.info("유효성 에러 발생!");
            return "view/apply_studygroup";
        }
        log.info("유효성 검사 끝!");

        studyGroupApplicationService.createNewStudyGroupApplication(studyGroupApplicationForm, member);
        return "redirect:/";
    }

    @GetMapping("/rc_answer_sheet")
    public String rcAnswerSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/rc_answer_sheet";
    }

    @GetMapping("/lc_answer_sheet")
    public String lcAnswerSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "detail";
    }

    @GetMapping("/spk_answer_sheet")
    public String spkAnswerSheet(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/spk_answer_sheet";
    }


    @GetMapping("/schedule")
    public String schedule(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/schedule";
    }

    @GetMapping("/my_progress")
    public String myProgress(@CurrentUser Member member, Model model) {
        QuestionSet qToeic = null;
        QuestionSet hToeic = null;
        QuestionSet fToeic = null;
        QuestionSet pToeic = null;

        if (member.getQuestionSetList() != null) {
            member = memberRepository.getOne(member.getId());
            List<QuestionSet> questionSetList = questionSetRepository.getAllByMember(member);
            for (QuestionSet qs : questionSetList) {
                if (qs.getQuestionSetType().toString() == "QUARTER_TOEIC") qToeic = qs;
                else if (qs.getQuestionSetType().toString() == "HALF_TOEIC") hToeic = qs;
                else if (qs.getQuestionSetType().toString() == "FULL_TOEIC") fToeic = qs;
                else if (qs.getQuestionSetType().toString() == "PRACTICE") pToeic = qs;
                else continue;
            }
        }

        if (qToeic != null) {
            model.addAttribute("qToeic", qToeic);
            model.addAttribute("qToeicComment", memberService.CreateProgressByQuestionSet(qToeic));
        }
        if (hToeic != null) {
            model.addAttribute("hToeic", hToeic);
            model.addAttribute("hToeicComment", memberService.CreateProgressByQuestionSet(hToeic));
        }
        if (fToeic != null) {
            model.addAttribute("fToeic", fToeic);
            model.addAttribute("fToeicComment", memberService.CreateProgressByQuestionSet(fToeic));
            switch ((int)(Float.parseFloat(questionSetService.getPercentage(fToeic))/10)){
                case 1 : case 2 : case 3 : case 4 :{
                    member.setLevelType(LevelType.BEGINNER);
                    break;
                }
                case 5 : case 6 : case 7: {
                    member.setLevelType(LevelType.INTERMEDIATE);
                    break;
                }
                case 8: case 9 : case 10 : {
                    member.setLevelType(LevelType.ADVANCED);
                }
            }

        }
        if (pToeic != null) {
            model.addAttribute("pToeic", pToeic);
            model.addAttribute("pToeicComment", memberService.CreateProgressByQuestionSet(pToeic));
        }

        model.addAttribute("part1", memberService.createCommentByQuestionType(member, QuestionType.PART1));
        model.addAttribute("part2", memberService.createCommentByQuestionType(member, QuestionType.PART2));
        model.addAttribute("part3", memberService.createCommentByQuestionType(member, QuestionType.PART3));
        model.addAttribute("part4", memberService.createCommentByQuestionType(member, QuestionType.PART4));
        model.addAttribute("part5", memberService.createCommentByQuestionType(member, QuestionType.PART5));
        model.addAttribute("part6", memberService.createCommentByQuestionType(member, QuestionType.PART6));
        model.addAttribute("part7s", memberService.createCommentByQuestionType(member, QuestionType.PART7_SINGLE_PARAGRAPH));
        model.addAttribute("part7m", memberService.createCommentByQuestionType(member, QuestionType.PART7_MULTIPLE_PARAGRAPH));
        model.addAttribute("level", memberService.CreateLevelByAllQuestions(member));
        model.addAttribute("member", member);

        return "view/my_progress";
    }

}