package toeicLab.toeicLab.service;

import com.sun.xml.bind.v2.util.QNameMap;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.MeetingRepository;
import toeicLab.toeicLab.repository.QuestionRepository;
import toeicLab.toeicLab.repository.QuestionSetRepository;
import toeicLab.toeicLab.repository.StudyGroupRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionSetService {

    private final int[] PART1 = {3, 6, 6};
    private final int[] PART2 = {4, 8, 25};
    private final int[] PART3 = {3, 6, 13}; // set수, set당 3문제
    private final int[] PART4 = {3, 6, 10}; // set수, set당 3문제
    private final int[] PART5 = {4, 8, 30};
    private final int[] PART6 = {2, 4, 4}; // set수, set당 4문제

    private final QuestionSetRepository questionSetRepository;
    private final QuestionService questionService;
    private final MeetingRepository meetingRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final QuestionRepository questionRepository;

    public QuestionSet createToeicSet(Member member, QuestionSetType questionSetType) {
        QuestionSet result = new QuestionSet();
        List<Question> questionList = new ArrayList<>();

        questionList.addAll(questionService.createQuestionList(QuestionType.PART1, PART1[questionSetType.get()]));
        questionList.addAll(questionService.createQuestionList(QuestionType.PART2, PART2[questionSetType.get()]));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART3, PART3[questionSetType.get()]));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART4, PART4[questionSetType.get()]));
        questionList.addAll(questionService.createQuestionList(QuestionType.PART5, PART5[questionSetType.get()]));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART6, PART6[questionSetType.get()]));
        questionList.addAll(questionService.createPart7ByQuestionSetType(questionSetType));

        result.setQuestions(questionList);
        result.setCreatedAt(LocalDateTime.now());
        result.setMember(member);
        result.setQuestionSetType(questionSetType);

        return result;
    }


    public QuestionSet createPracticeLC(Member member, int p1, int p2, int p3, int p4) {
        QuestionSet result = new QuestionSet();
        List<Question> questionList = new ArrayList<>();
        if (p1 > 0) questionList.addAll(questionService.createQuestionList(QuestionType.PART1, p1));
        if (p2 > 0) questionList.addAll(questionService.createQuestionList(QuestionType.PART2, p2));
        if (p3 > 0) questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART3, p3));
        if (p4 > 0) questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART4, p4));
        result.setQuestions(questionList);
        result.setCreatedAt(LocalDateTime.now());
        result.setMember(member);
        result.setQuestionSetType(QuestionSetType.PRACTICE);
        return result;
    }

    public QuestionSet createPracticeRC(Member member, int p5, int p6, int p7s, int p7m) {
        QuestionSet result = new QuestionSet();
        List<Question> questionList = new ArrayList<>();
        if (p5 > 0) questionList.addAll(questionService.createQuestionList(QuestionType.PART5, p5));
        if (p6 > 0) questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART6, p6));
        if (p7s > 0)
            questionList.addAll(questionService.createQuestionByQuestionTypeAndNumber(QuestionType.PART7_SINGLE_PARAGRAPH, p7s));
        if (p7m > 0)
            questionList.addAll(questionService.createQuestionByQuestionTypeAndNumber(QuestionType.PART7_MULTIPLE_PARAGRAPH, p7m));
        result.setQuestions(questionList);
        result.setCreatedAt(LocalDateTime.now());
        result.setMember(member);
        result.setQuestionSetType(QuestionSetType.PRACTICE);
        return result;
    }

    public void createMeeting(StudyGroup studyGroup, int[] numberOfQuestions, String date) {
        QuestionSet meetingQuestionSet1 = new QuestionSet();
        QuestionSet meetingQuestionSet2 = new QuestionSet();
        QuestionSet meetingQuestionSet3 = new QuestionSet();
        QuestionSet meetingQuestionSet4 = new QuestionSet();


        List<Question> questionList = new ArrayList<>();

        int part1 = numberOfQuestions[0];
        int part2 = numberOfQuestions[1];
        int part3 = numberOfQuestions[2];
        int part4 = numberOfQuestions[3];
        int part5 = numberOfQuestions[4];
        int part6 = numberOfQuestions[5];
        int part7_Single = numberOfQuestions[6];
        int part7_Multiple = numberOfQuestions[7];


        questionList.addAll(questionService.createQuestionList(QuestionType.PART1, part1));
        questionList.addAll(questionService.createQuestionList(QuestionType.PART2, part2));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART3, part3));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART4, part4));
        questionList.addAll(questionService.createQuestionList(QuestionType.PART5, part5));
        questionList.addAll(questionService.createQuestionListWithSmallSet(QuestionType.PART6, part6));
        questionList.addAll(questionService.createQuestionByQuestionTypeAndNumber(QuestionType.PART7_SINGLE_PARAGRAPH, part7_Single));
        questionList.addAll(questionService.createQuestionByQuestionTypeAndNumber(QuestionType.PART7_MULTIPLE_PARAGRAPH, part7_Multiple));

        meetingQuestionSet1.setQuestions(questionList);
        meetingQuestionSet1.setCreatedAt(LocalDateTime.now());
        meetingQuestionSet1.setQuestionSetType(QuestionSetType.MEETING);

        meetingQuestionSet2.setQuestions(questionList);
        meetingQuestionSet2.setCreatedAt(LocalDateTime.now());
        meetingQuestionSet2.setQuestionSetType(QuestionSetType.MEETING);

        meetingQuestionSet3.setQuestions(questionList);
        meetingQuestionSet3.setCreatedAt(LocalDateTime.now());
        meetingQuestionSet3.setQuestionSetType(QuestionSetType.MEETING);

        meetingQuestionSet4.setQuestions(questionList);
        meetingQuestionSet4.setCreatedAt(LocalDateTime.now());
        meetingQuestionSet4.setQuestionSetType(QuestionSetType.MEETING);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(date, formatter);

        studyGroup = studyGroupRepository.findById(studyGroup.getId()).orElse(null);
        int count = 0;
        assert studyGroup != null;
        if (studyGroup.getMeetings() != null) {
            count = studyGroup.getMeetings().size() + 1;
        }

        Meeting meeting = Meeting.builder()
                .count(count)
                .studyGroup(studyGroup)
                .date(dateTime.atStartOfDay())
                .build();

        meetingQuestionSet1.setMember(studyGroup.getMembers().get(0));
        questionSetRepository.save(meetingQuestionSet1);
        meeting.setQuestionSet1(meetingQuestionSet1);

        meetingQuestionSet2.setMember(studyGroup.getMembers().get(1));
        questionSetRepository.save(meetingQuestionSet2);
        meeting.setQuestionSet2(meetingQuestionSet2);

        meetingQuestionSet3.setMember(studyGroup.getMembers().get(2));
        questionSetRepository.save(meetingQuestionSet3);
        meeting.setQuestionSet3(meetingQuestionSet3);

        meetingQuestionSet4.setMember(studyGroup.getMembers().get(3));
        questionSetRepository.save(meetingQuestionSet4);
        meeting.setQuestionSet4(meetingQuestionSet4);

        meetingRepository.save(meeting);
    }

    public QuestionSet findQuestionSet(Long setId) {
        Optional<QuestionSet> optional = questionSetRepository.findById(setId);
        return optional.orElse(null);

    }

    public int[] selectFormToArray(String[] select_form) {

        int[] result = new int[8];

        for (int i = 1; i < select_form.length; i = i + 3) {
            switch (select_form[i]) {
                case "PART1":
                    result[0] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART2":
                    result[1] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART3":
                    result[2] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART4":
                    result[3] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART5":
                    result[4] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART6":
                    result[5] = Integer.parseInt(select_form[i + 1]);
                    break;
                case "PART7":
                    result[6] = (int) (Math.random() * Integer.parseInt(select_form[i + 1]));
                    result[7] = Integer.parseInt(select_form[i + 1]) - result[6];
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    public String getPercentage(QuestionSet questionSet) {
        int correctCount = 0;
        int totalCount = questionSet.getQuestions().size();
        Map<Long, String> submittedAnswersForQs = questionSet.getSubmittedAnswers();
        for (Map.Entry<Long, String> entry : submittedAnswersForQs.entrySet()) {
            Question question = questionRepository.getOne(entry.getKey());
            if (question.getAnswer().equals(entry.getValue())) {
                correctCount++;
            }
        }
        String str = Integer.toString(correctCount * 100 / totalCount);
        return str + "%";
    }

    public String[] getPercentageEachOfQuestionSet(QuestionSet questionSet) {
        String[] comments = new String[8];

        int[] part1 = new int[2];
        int[] part2 = new int[2];
        int[] part3 = new int[2];
        int[] part4 = new int[2];
        int[] part5 = new int[2];
        int[] part6 = new int[2];
        int[] part7 = new int[2];

        Map<Long, String> submittedAnswersForQs = questionSet.getSubmittedAnswers();
        for (Map.Entry<Long, String> entry : submittedAnswersForQs.entrySet()) {
            Question question = questionRepository.getOne(entry.getKey());
            if (question.getQuestionType().toString().equals("PART1")) {
                part1[0]++;
                if (question.getAnswer().equals(entry.getValue())) part1[1]++;
            } else if (question.getQuestionType().toString().equals("PART2")) {
                part2[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part2[1]++;
            } else if (question.getQuestionType().toString().equals("PART3")) {
                part3[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part3[1]++;
            } else if (question.getQuestionType().toString().equals("PART4")) {
                part4[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part4[1]++;
            } else if (question.getQuestionType().toString().equals("PART5")) {
                part5[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part5[1]++;
            } else if (question.getQuestionType().toString().equals("PART6")) {
                part6[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part6[1]++;
            } else if (question.getQuestionType().toString().equals("PART7_SINGLE_PARAGRAPH") || question.getQuestionType().toString().equals("PART7_MULTIPLE_PARAGRAPH")) {
                part7[0]++;
                if ((question.getAnswer().equals(entry.getValue()))) part7[1]++;
            }
        }

        comments[0] = getPercentage(questionSet);

        try {
            comments[1] = part1[1] + " / " + part1[0] + " = " + (part1[1] * 100 / part1[0]) + "%";
        } catch (ArithmeticException e) {
            comments[1] = null;
        }
        try {
            comments[2] = part2[1] + " / " + part2[0] + " = " + (part2[1] * 100 / part2[0]) + "%";
        } catch (ArithmeticException e) {
            comments[2] = null;
        }
        try {
            comments[3] = part3[1] + " / " + part3[0] + " = " + (part3[1] * 100 / part3[0]) + "%";
        } catch (ArithmeticException e) {
            comments[3] = null;
        }
        try {
            comments[4] = part4[1] + " / " + part4[0] + " = " + (part4[1] * 100 / part4[0]) + "%";
        } catch (ArithmeticException e) {
            comments[4] = null;
        }
        try {
            comments[5] = part5[1] + " / " + part5[0] + " = " + (part5[1] * 100 / part5[0]) + "%";
        } catch (ArithmeticException e) {
            comments[5] = null;
        }
        try {
            comments[6] = part6[1] + " / " + part6[0] + " = " + (part6[1] * 100 / part6[0]) + "%";
        } catch (ArithmeticException e) {
            comments[6] = null;
        }
        try {
            comments[7] = part7[1] + " / " + part7[0] + " = " + (part7[1] * 100 / part7[0]) + "%";
        } catch (ArithmeticException e) {
            comments[7] = null;
        }

        return comments;
    }
}
