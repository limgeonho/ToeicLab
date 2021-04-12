package toeicLab.toeicLab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {

        return questionRepository.findAll();
    }

    public List<Question> createQuestionList(QuestionType questionType, int numberOfQuestions) {
        List<Question> allQuestionList = questionRepository.findAllByQuestionType(questionType);
        List<Question> result = new ArrayList<>();

        for (int i = 1; i <= numberOfQuestions; ++i) {
            Question question = allQuestionList.get((int) (Math.random() * allQuestionList.size()));
            if (result.contains(question)) {
                --i;
                continue;
            }
            result.add(question);
        }

        return result;
    }

    public List<Question> createQuestionListWithSmallSet(QuestionType questionType, int numberOfSmallSets) {
        List<Question> allQuestionList = questionRepository.findAllByQuestionType(questionType);
        List<Question> result = new ArrayList<>();
        List<Question> duplicateCheckList = new ArrayList<>();
        for (int i = 1; i <= numberOfSmallSets; ++i) {
            Question question = allQuestionList.get((int) (Math.random() * allQuestionList.size()));
            if (!duplicateCheckList.contains(question)) {
                duplicateCheckList.add(question);
            }
            result.addAll(questionRepository.findAllBySmallSetId(question.getSmallSetId()));
        }
        return result;
    }

    public List<Question> createPart7ByQuestionSetType(QuestionSetType questionSetType) {
        int[] smallSet_Single = new int[3];
        int[] smallSet_Multiple = new int[3];

        switch (questionSetType.get()) {
            case 0: // quarter
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        smallSet_Single = new int[]{0, 1, 1};
                        smallSet_Multiple = new int[]{0, 1, 0};
                        break;
                    case 1:
                        smallSet_Single = new int[]{1, 0, 1};
                        smallSet_Multiple = new int[]{0, 0, 1};
                        break;
                }
                break;

            case 1: // half
                switch ((int) (Math.random() * 3)) {
                    case 0:
                        smallSet_Single = new int[]{1, 1, 2};
                        smallSet_Multiple = new int[]{0, 1, 1};
                        break;
                    case 1:
                        smallSet_Single = new int[]{0, 4, 0};
                        smallSet_Multiple = new int[]{0, 0, 2};
                        break;
                    case 2:
                        smallSet_Single = new int[]{0, 2, 2};
                        smallSet_Multiple = new int[]{1, 0, 1};
                        break;
                }
                break;

            case 2: // full
                switch ((int) (Math.random() * 6)) {
                    case 0:
                        smallSet_Single = new int[]{0, 1, 5};
                        break;
                    case 1:
                        smallSet_Single = new int[]{3, 0, 4};
                        break;
                    case 2:
                        smallSet_Single = new int[]{2, 2, 3};
                        break;
                    case 3:
                        smallSet_Single = new int[]{1, 4, 2};
                        break;
                    case 4:
                        smallSet_Single = new int[]{5, 1, 2};
                        break;
                    case 5:
                        smallSet_Single = new int[]{4, 3, 1};
                        break;
                }
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        smallSet_Multiple = new int[]{0, 0, 5};
                        break;
                    case 1:
                        smallSet_Multiple = new int[]{5, 0, 2};
                        break;
                    case 2:
                        smallSet_Multiple = new int[]{0, 5, 1};
                        break;
                    case 3:
                        smallSet_Multiple = new int[]{3, 4, 0};
                        break;
                }
                break;
        }

        List<Question> singleList = createPart7byQuestionTypeAndNumber(QuestionType.PART7_SINGLE_PARAGRAPH,
                smallSet_Single[0], smallSet_Single[1], smallSet_Single[2]);
        List<Question> multipleList = createPart7byQuestionTypeAndNumber(QuestionType.PART7_MULTIPLE_PARAGRAPH,
                smallSet_Multiple[0], smallSet_Multiple[1], smallSet_Multiple[2]);

        List<Question> result = new ArrayList<>(singleList);
        result.addAll(multipleList);
        return result;
    }

    public List<Question> createPart7byQuestionTypeAndNumber(QuestionType questionType, int numberOfSmallSetType3,
                                                             int numberOfSmallSetType4, int numberOfSmallSetType5) {
        List<Question> result = new ArrayList<>();

        for (int i = 1; i <= numberOfSmallSetType3; ++i) {
            List<Question> list = questionRepository.findAllByQuestionTypeAndSmallSetType(questionType, 3);
            Question question = list.get((int) (Math.random() * list.size()));
            if (result.contains(question)) {
                --i;
            }
            result.addAll(questionRepository.findAllBySmallSetId(question.getSmallSetId()));
        }

        for (int i = 1; i <= numberOfSmallSetType4; ++i) {
            List<Question> list = questionRepository.findAllByQuestionTypeAndSmallSetType(questionType, 4);
            Question question = list.get((int) (Math.random() * list.size()));
            if (result.contains(question)) {
                --i;
            }
            result.addAll(questionRepository.findAllBySmallSetId(question.getSmallSetId()));
        }

        for (int i = 1; i <= numberOfSmallSetType5; ++i) {
            List<Question> list = questionRepository.findAllByQuestionTypeAndSmallSetType(questionType, 5);
            Question question = list.get((int) (Math.random() * list.size()));
            if (result.contains(question)) {
                --i;
            }
            result.addAll(questionRepository.findAllBySmallSetId(question.getSmallSetId()));
        }
        return result;
    }

    public List<Question> createQuestionByQuestionTypeAndNumber(QuestionType questionType, int num) {
        List<Question> result = new ArrayList<>();

        for (int i = 1; i <= num; ++i) {
            List<Question> list = questionRepository.findAllByQuestionType(questionType);
            Question question = list.get((int) (Math.random() * list.size()));
            if (result.contains(question)) {
                --i;
            }
            result.addAll(questionRepository.findAllBySmallSetId(question.getSmallSetId()));
        }

        return result;
    }
    public void checkTypeList(QuestionSet questionSet, List<String> str) {
        for (Question q : questionSet.getQuestions()){
            if(q.getQuestionType().toString().equals("PART1")){
                if(!str.contains("PART1")){
                    str.add("PART1");
                } else {
                    continue;
                }
            }
            if(q.getQuestionType().toString().equals("PART2")){
                if(!str.contains("PART2")){
                    str.add("PART2");
                } else {
                    continue;
                }
            }
            if(q.getQuestionType().toString().equals("PART3")){
                if(!str.contains("PART3")){
                    str.add("PART3");
                } else {
                    continue;
                }
            }
            if(q.getQuestionType().toString().equals("PART4")){
                if(!str.contains("PART4")){
                    str.add("PART4");
                } else {
                    continue;
                }
            }
            if(q.getQuestionType().toString().equals("PART5")){
                if(!str.contains("PART5")){
                    str.add("PART5");
                } else {
                    continue;
                }
            }if(q.getQuestionType().toString().equals("PART6")){
                if(!str.contains("PART6")){
                    str.add("PART6");
                } else {
                    continue;
                }
            }
            if (q.getQuestionType().toString().equals("PART7_SINGLE_PARAGRAPH")){
                if(!str.contains("PART7_SINGLE_PARAGRAPH")){
                    str.add("PART7_SINGLE_PARAGRAPH");
                } else {
                    continue;
                }
            }
            if (q.getQuestionType().toString().equals("PART7_MULTIPLE_PARAGRAPH")){
                if(!str.contains("PART7_MULTIPLE_PARAGRAPH")){
                    str.add("PART7_MULTIPLE_PARAGRAPH");
                }
            }
        }
    }

    public Question findQuestion(Long id) {
        Optional<Question> optional= questionRepository.findById(id);
        return optional.orElse(null);
    }


    public Question createQuestion(String questionType, String content, String content2, String content3, String question, String exampleA, String exampleB, String exampleC, String exampleD, String answer, String solution) {
        QuestionType type=null;
        Question result = new Question();

        switch (questionType){
            case "part1" : {
                type = QuestionType.PART1;
                break;
            }
            case "part2" : {
                type = QuestionType.PART2;
                break;
            }
            case "part3" : {
                type = QuestionType.PART3;
                break;
            }
            case "part4" : {
                type = QuestionType.PART4;
                break;
            }
            case "part5" : {
                type = QuestionType.PART5;
                break;
            }
            case "part6" : {
                type = QuestionType.PART6;
                break;
            }
            case "part7" : {
                if (content2.length()>10){
                    type = QuestionType.PART7_MULTIPLE_PARAGRAPH;
                } else type = QuestionType.PART7_SINGLE_PARAGRAPH;
                break;
            }

        }

        if (type==QuestionType.PART1||type==QuestionType.PART2||type==QuestionType.PART3||type==QuestionType.PART4){
            LC lc = new LC();
            lc.setExampleA(exampleA);
            lc.setExampleB(exampleB);
            lc.setExampleC(exampleC);
            lc.setExampleD(exampleD);
            lc.setSolution(solution);
            lc.setQuestionType(type);
            lc.setAnswer(answer);
            lc.setQuestionExplanation(question);
            result = questionRepository.save(lc);
        } else {
            RC rc = new RC();
            rc.setExampleA(exampleA);
            rc.setExampleB(exampleB);
            rc.setExampleC(exampleC);
            rc.setExampleD(exampleD);
            rc.setSolution(solution);
            rc.setContent(content);
            rc.setContent2(content2);
            rc.setContent3(content3);
            rc.setAnswer(answer);
            rc.setQuestionType(type);
            rc.setQuestionExplanation(question);
            result = questionRepository.save(rc);
        }


        return result;
    }
}
