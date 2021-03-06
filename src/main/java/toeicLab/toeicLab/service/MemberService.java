package toeicLab.toeicLab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.*;
import toeicLab.toeicLab.user.MemberUser;
import toeicLab.toeicLab.user.SignUpForm;
import toeicLab.toeicLab.user.UpdateForm;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewNoteRepository reviewNoteRepository;
    private final QuestionRepository questionRepository;
    private final QuestionSetRepository questionSetRepository;
    private final WordRepository wordRepository;
    private final QuestionSetService questionSetService;


    public Member createNewMember(SignUpForm signUpForm) {
        Member member = Member.builder()
                .userId(signUpForm.getUserId())
                .password("{noop}" + signUpForm.getPassword())
                .nickname(signUpForm.getNickname())
                .role("ROLE_USER")
                .age(signUpForm.getAge())
                .provider("toeicLab")
                .email(signUpForm.getEmail())
                .contact(signUpForm.getContact())
                .address(Address.builder()
                        .zipcode(signUpForm.getZipcode())
                        .city(signUpForm.getCity())
                        .street(signUpForm.getStreet())
                        .build())
                .memberType(MemberType.USER)
                .build();

        if(signUpForm.getGender().equals("male")){
            member.setGenderType(GenderType.MALE);
        }else {
            member.setGenderType(GenderType.FEMALE);
        }

//        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId);
        log.info("[ToeicLab]?????? ?????????" + member.getUserId());

        if(member == null) {
            throw new UsernameNotFoundException(userId);
        }
        log.info(member.getUserId());
        return new MemberUser(member);
    }

    public void autologin(Member member) {

        MemberUser memberUser = new MemberUser(member);

        // ?????? ?????? ??????
        // SecurityContext ??? ?????? ????????? ?????? (?????? ????????? ?????? ???????????? ??????)
        // ?????? ????????? AuthenticationToken ?????? ?????????.
        // Token ?????? : username, password, role ?????? ?????????.
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser, //member.getEmail(),  // username
                        memberUser.getMember().getPassword(), // password
                        memberUser.getAuthorities() // authorities (????????? (Collection ??????))
                );

        // SecurityContext ??? token ??????
        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(token);

    }


    public Member sendResetPasswordEmail(String userId, String email) {
        Member member = memberRepository.findByUserId(userId);

        if(member == null){
            throw new IllegalArgumentException("???????????? ?????? ??????????????????.");
        }

        boolean emailCheck = member.getEmail().equals(email);

        if(!emailCheck){
            throw new IllegalArgumentException("???????????? ?????? ??????????????????.");
        }

        return member;
    }

    public void resetPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        member.setPassword("{noop}" + password);
//        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
    }


    public Member sendFindIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw new IllegalArgumentException("???????????? ?????? ??????????????????.");
        }
        return member;
    }

    @Transactional
    public boolean addReviewNote(Member member, Long questionId, String answer) {
        Question question = questionRepository.getOne(questionId);
        ReviewNote reviewNote = reviewNoteRepository.findByMember(member);
        if (reviewNote == null){
            reviewNote = new ReviewNote();
            reviewNote.setMember(member);
        }
        List<Question> qlist = reviewNote.getQuestions();
        Map<Long, String> alist = reviewNote.getSubmittedAnswers();

        if(qlist.contains(question)){
            return false;
        } else {

            qlist.add(question);
            alist.put(questionId, answer);
            reviewNote.setQuestions(qlist);
            reviewNote.setSubmittedAnswers(alist);
            reviewNoteRepository.save(reviewNote);
            return true;
        }
    }

    public void deleteReviewNote(Member member, Long questionId) {
        Question question = questionRepository.getOne(questionId);
        ReviewNote reviewNote = reviewNoteRepository.findByMember(member);
        List<Question> list = reviewNote.getQuestions();
        Map <Long, String> alist = reviewNote.getSubmittedAnswers();
        for(int i=0; i<list.size(); ++i){
            if(list.get(i) == question){
                list.remove(i);
                alist.remove(i);

                reviewNote.setQuestions(list);
                reviewNote.setSubmittedAnswers(alist);
                reviewNoteRepository.save(reviewNote);
            }
        }
        return;
    }

    public int[] numberOfCorrectAndWrongAnswersByQuestionType(Member member, QuestionType questionType) {

        List<QuestionSet> questionSets = questionSetRepository.getAllByMember(member);
        int correctCount = 0;
        int totalCount = 0;

        for (QuestionSet qs : questionSets) {
            Map<Long, String> submittedAnswersForQs = qs.getSubmittedAnswers();
            for (Map.Entry<Long, String> entry : submittedAnswersForQs.entrySet()) {
                Question question = questionRepository.getOne(entry.getKey());
                if (question.getQuestionType() == questionType) {
                    totalCount++;
                    if(question.getAnswer().equals(entry.getValue())){
                        correctCount++;
                    }
                }
            }
        }
        return new int[]{correctCount, totalCount - correctCount};
    }



    public boolean addWordList(Member member, String word, String meaning) {
        Word wordList = wordRepository.findByMember(member);
        if (wordList == null){
            wordList = new Word();
            wordList.setMember(member);
        }
        System.out.println(word);
        System.out.println(meaning);
        Map<String, String> map = wordList.getWord();
        for (Map.Entry<String, String> m : map.entrySet()) {
            if (m.getKey().contains(word)) {
                return false;
            }
        }
        map.put(word, meaning);
        wordList.setWord(map);
        wordRepository.save(wordList);
        return true;
    }

    public void deleteWord(Member member, String word) {
        Word wordList = wordRepository.findByMember(member);
        Map<String, String> map = wordList.getWord();
        map.remove(word);
        wordList.setWord(map);
        wordRepository.save(wordList);
    }


//    public boolean deleteWord(Member member, String word) {
//        Word wordList = wordRepository.findByMember(member);
//        Map<String, String> map = wordList.getWord();
//        map.remove(word);
//        wordList.setWord(map);
//        wordRepository.save(wordList);
//        return true;
//    }


    public String createCommentByQuestionType(Member member, QuestionType questionType) {
        String comment = "????????? ???????????? ????????????";
        int[] correctAndWrong = numberOfCorrectAndWrongAnswersByQuestionType(member, questionType);
        int total = correctAndWrong[0] + correctAndWrong[1];
        if (total == 0) {
            return comment;
        }
        int percentage = correctAndWrong[0] / total * 100;
        if (total < 10) {
            comment = "?????? " + total + "?????? ???????????????. ????????? ????????? ?????? ??? ?????? ????????? ?????????.";
        } else {
            if (percentage > 80) {
                comment = "???????????? " + percentage + "% ??? ?????? ????????????.";
            }
            if (percentage < 50) {
                comment = "???????????? " + percentage + "% ??? ?????? ????????????.";
            }
        }
        return comment;
    }


    public String CreateProgressByQuestionSet(QuestionSet questionSet){
        String date = questionSet.getCreatedAt().format(DateTimeFormatter.ofPattern("yy???MM???dd???HH???"));
        String percentage = questionSetService.getPercentage(questionSet);
        return date + "??? ????????? ????????? ????????? : " + percentage;
    }

    public String CreateLevelByAllQuestions(Member member) {
        int total = 0;
        int correct = 0;
        List<QuestionSet> questionSetList = questionSetRepository.getAllByMember(member);
        if(questionSetList == null) return "?????? ?????? ???????????? ????????????.";
        for (QuestionSet qs : questionSetList) {
            if (qs.getQuestionSetType().toString().equals("PRACTICE") ||qs.getQuestionSetType().toString().equals("MEETING")) continue;
            Map<Long, String> submittedAnswersForQs = qs.getSubmittedAnswers();
            for (Map.Entry<Long, String> entry : submittedAnswersForQs.entrySet()) {
                Question question = questionRepository.getOne(entry.getKey());
                total++;
                if (question.getAnswer().equals(entry.getValue())) {
                    correct++;
                }
            }
        }
        if(total==0) return "???????????? ????????????.";


        String level = null;
        if (correct * 100 / total < 30) level ="???";
        if (correct * 100 / total >= 30 && correct * 100 / total < 80 ) level = "???";
        if (correct * 100 / total >= 80) level = "???";

        return level;
    }

}
