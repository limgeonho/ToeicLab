package toeicLab.toeicLab.controller;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.*;
import toeicLab.toeicLab.service.MemberService;
import toeicLab.toeicLab.service.QuestionService;
import toeicLab.toeicLab.service.QuestionSetService;
import toeicLab.toeicLab.user.CurrentUser;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionSetService questionSetService;
    private final QuestionSetRepository questionSetRepository;
    private final QuestionService questionService;
    private final MemberRepository memberRepository;
    private final ReviewNoteRepository reviewNoteRepository;
    private final MemberService memberService;
    private final MeetingRepository meetingRepository;

    @GetMapping("/practice_select/{id}")
    public String practiceTest(@CurrentUser Member member, @PathVariable String id, Model model){
        if(id.equals("rc")) model.addAttribute("practice", "rc");
        if(id.equals("lc")) model.addAttribute("practice", "lc");
        if(id.equals("spk")) model.addAttribute("practice", "spk");
        model.addAttribute("member", member);
        return "view/practice_select";
    }

    @RequestMapping("/practice/{id}")
    @Transactional
    public String test(@CurrentUser Member member, @PathVariable String id, HttpServletRequest request, Model model){
        model.addAttribute("member", member);
        QuestionSet list;
        if(("lc").equals(id)){
            int p1 = Integer.parseInt(request.getParameter("PART1"));
            int p2 = Integer.parseInt(request.getParameter("PART2"));
            int p3 = Integer.parseInt(request.getParameter("PART3"));
            int p4 = Integer.parseInt(request.getParameter("PART4"));
            if(p1 > 0) model.addAttribute("part1List", true);
            if(p2 > 0) model.addAttribute("part2List", true);
            if(p3 > 0) model.addAttribute("part3List", true);
            if(p4 > 0) model.addAttribute("part4List", true);
            list = questionSetService.createPracticeLC(member, p1, p2, p3, p4);
            questionSetRepository.save(list);
            model.addAttribute("questionList", list.getQuestions());
            model.addAttribute("questionSet", list);
            return "view/practice_sheet";
        }
        else if (("rc").equals(id)){
            int p5 = Integer.parseInt(request.getParameter("PART5"));
            int p6 = Integer.parseInt(request.getParameter("PART6"));
            int p7s = Integer.parseInt(request.getParameter("PART7-single"));
            int p7m = Integer.parseInt(request.getParameter("PART7-multiple"));
            if(p5 > 0) model.addAttribute("part5List", true);
            if(p6 > 0) model.addAttribute("part6List", true);
            if(p7s > 0) model.addAttribute("part7sList", true);
            if(p7m > 0) model.addAttribute("part7mList", true);
            list = questionSetService.createPracticeRC(member, p5, p6, p7s, p7m);
            questionSetRepository.save(list);
            model.addAttribute("questionList", list.getQuestions());
            model.addAttribute("questionSet", list);
            return "view/practice_sheet";
        }
        else {
            Meeting meeting = meetingRepository.getOne(Long.parseLong(id));
            QuestionSet qs1 = meeting.getQuestionSet1();
            QuestionSet qs2 = meeting.getQuestionSet2();
            QuestionSet qs3 = meeting.getQuestionSet3();
            QuestionSet qs4 = meeting.getQuestionSet4();

            QuestionSet questionSet = new QuestionSet();

            if(qs1.getMember().getId().equals(member.getId())) questionSet = qs1;
            if(qs2.getMember().getId().equals(member.getId())) questionSet = qs2;
            if(qs3.getMember().getId().equals(member.getId())) questionSet = qs3;
            if(qs4.getMember().getId().equals(member.getId())) questionSet = qs4;

            List<Question> questions = questionSet.getQuestions();
            for(Question q : questions){
                if(q.getQuestionType().toString().equals("PART1")) model.addAttribute("part1List", true);
                else if(q.getQuestionType().toString().equals("PART2")) model.addAttribute("part2List", true);
                else if(q.getQuestionType().toString().equals("PART3")) model.addAttribute("part3List", true);
                else if(q.getQuestionType().toString().equals("PART4")) model.addAttribute("part4List", true);
                else if(q.getQuestionType().toString().equals("PART5")) model.addAttribute("part5List", true);
                else if(q.getQuestionType().toString().equals("PART6")) model.addAttribute("part6List", true);
                else if(q.getQuestionType().toString().equals("PART7_SINGLE_PARAGRAPH")) model.addAttribute("part7sList", true);
                else if(q.getQuestionType().toString().equals("PART7_MULTIPLE_PARAGRAPH")) model.addAttribute("part7mList", true);
            }
            model.addAttribute("questionList", questions);
            model.addAttribute("questionSet", questionSet);
            model.addAttribute("studyGroupId", meeting.getStudyGroup().getId());
            System.out.println(meeting.getStudyGroup().getId());
            return "view/practice_sheet";
        }
    }

    @GetMapping("/test/{type}")
    @Transactional
    public String test1 (@CurrentUser Member member, @PathVariable String type, Model model){
        QuestionSet list = new QuestionSet();
        switch (type) {
            case "Quarter":
                list = questionSetService.createToeicSet(member, QuestionSetType.QUARTER_TOEIC);
                break;

            case "Half":
                list = questionSetService.createToeicSet(member, QuestionSetType.HALF_TOEIC);
                break;

            case "Full":
                list = questionSetService.createToeicSet(member, QuestionSetType.FULL_TOEIC);
                break;

            default:
                break;
        }
        questionSetRepository.save(list);
        model.addAttribute("questionSet", list);
        model.addAttribute("questionList", list.getQuestions());
        model.addAttribute("type", type);
        model.addAttribute("member", member);
        return "view/question/test";
    }

    @PostMapping("/result_sheet/{questionSetId}")
    public String resultSheet(@CurrentUser Member member, HttpServletRequest request, @PathVariable Long questionSetId, Model model) {
        QuestionSet questionSet = null;
        Map<Long, String> map = new HashMap<>();
        Enumeration en = request.getParameterNames();
        String param;
        String value;
        long setIdValue = 0;
        while (en.hasMoreElements()) {
            param = (String) en.nextElement();
            value = request.getParameter(param);
            if (param.equals("_csrf")) {
                continue;
            }
            if (param.equals("questionSetId")) {
                questionSet = questionSetRepository.getOne(Long.parseLong(value));
                setIdValue = Long.parseLong(value);
                continue;
            }
            if (param.equals("studyGroupId")) {
                model.addAttribute("studyGroupId", value);
                continue;
            }
            map.put(Long.parseLong(param), value);
        }
        /*===============================================*/
        System.out.println(map);
        assert questionSet != null;
        questionSet.setSubmittedAnswers(map);
        questionSetRepository.save(questionSet);
        questionSet = questionSetRepository.getOne(setIdValue);

        /*=================================PART 걸러내기===============================*/
        List<String> str = new ArrayList<>();
        questionService.checkTypeList(questionSet, str);
        System.out.println(str);
        /*============================================================================*/

        model.addAttribute("questionType", str);
        model.addAttribute("questionList", questionSet.getQuestions());
        model.addAttribute("questionSet", questionSet);
        model.addAttribute("userAnswer", map);
        model.addAttribute("member", member);
        model.addAttribute("questionSetId", questionSetId);

        String[] eachstrings = questionSetService.getPercentageEachOfQuestionSet(questionSet);
        model.addAttribute("eachstrings", eachstrings);

        return "view/result_sheet";
    }

    @RequestMapping( "/detail/{id}")
    public String lcAnswerSheet(@CurrentUser Member member, @PathVariable Long id, HttpServletRequest request, Model model) {
        System.out.println(request.getParameter("questionSetId"));
        long questionSetId = Long.parseLong(request.getParameter("questionSetId"));
        QuestionSet questionSet = questionSetRepository.getOne(questionSetId);

        log.info("id: " + id);
        Question question= questionService.findQuestion(id);

        if(member!=null){
            member = memberRepository.findByEmail(member.getEmail());
        }
        System.out.println(questionSet.getSubmittedAnswers());
        model.addAttribute("question", question);
        model.addAttribute("member", member);
        model.addAttribute("questionSetId", questionSetId);
        model.addAttribute("userAnswer", questionSet.getSubmittedAnswers());
        return "view/detail";
    }

    @GetMapping("/my_review_note")
    public String myReviewNote(@CurrentUser Member member, Model model) {
        ReviewNote reviewNote = reviewNoteRepository.findByMember(member);
        if(reviewNote == null){
            reviewNote = new ReviewNote();
            model.addAttribute("exist", "noReviewNote");
        }
        List<Question> list = reviewNote.getQuestions();
        if(list.isEmpty()){
            model.addAttribute("exist", "noQuestion");
        }
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestions(list);
        List<String> str = new ArrayList<>();
        questionService.checkTypeList(questionSet, str);

        model.addAttribute("questionType", str);
        model.addAttribute("questionList", list);
        model.addAttribute("member", member);
        model.addAttribute("userAnswer", reviewNote.getSubmittedAnswers());
        return "view/my_review_note";
    }

    @GetMapping("/add_review_note")
    @ResponseBody
    public String AddReviewNote(@CurrentUser Member member, @RequestParam("id") Long id, @RequestParam("answer") String answer){
        JsonObject jsonObject = new JsonObject();
        boolean result = false;
        try {
            result = memberService.addReviewNote(member, id, answer);
            if(result) {
                jsonObject.addProperty("message", "오답노트에 추가되었습니다.");
            }
            else {
                jsonObject.addProperty("message", "오답노트에 이미 있습니다.");
            }

        } catch (IllegalArgumentException e){
            jsonObject.addProperty("message", "잘못된정보");
        }
        return jsonObject.toString();
    }

    @GetMapping("/delete_review_note")
    @ResponseBody
    public String TestReviewNote(@CurrentUser Member member, @RequestParam("id") Long id){
        JsonObject jsonObject = new JsonObject();
        try {
            memberService.deleteReviewNote(member, id);
            jsonObject.addProperty("message", "오답노트에서 삭제되었습니다.");
        }
        catch (Exception e){
            jsonObject.addProperty("message", "예상치 못한 오류");
        }
        return jsonObject.toString();
    }
}
