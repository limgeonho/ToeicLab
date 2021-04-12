package toeicLab.toeicLab.controller;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toeicLab.toeicLab.domain.*;
import toeicLab.toeicLab.repository.ForumRepository;
import toeicLab.toeicLab.repository.QuestionRepository;
import toeicLab.toeicLab.repository.WordRepository;
import toeicLab.toeicLab.service.*;
import toeicLab.toeicLab.user.CurrentUser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ForumController {

    private final MemberService memberService;
    private final WordRepository wordRepository;
    private final ForumRepository forumRepository;
    private final QuestionService questionService;
    private final ForumService forumService;
    private final QuestionRepository questionRepository;
    private final VisionService visionService;

    @GetMapping("/forum")
    public String forum(@CurrentUser Member member, Model model, @RequestParam(defaultValue = "1") int page) {
        // 총 게시물 수
        List<Forum> forumList = forumRepository.findAll();

        int size = forumList.size();
        log.info("리스트의 개수");
        log.info(String.valueOf(size));
        int totalListCnt = size;

        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        int pageCheck = startIndex + pageSize;

        log.info("pageCheck=" + pageCheck);
        List<Forum> forumList2 = new ArrayList<>();

        if(totalListCnt == 0){
            log.info("마지막페이지=" + pagination.getEndPage());
            // ============================전체페이지가 1일때 설정============================
            pagination.setEndPage(1);
            pagination.setNextBlock(1);
            pagination.setTotalPageCnt(1);
            // ===========================================================================
            model.addAttribute(member);
            model.addAttribute("forumList", forumList2);
            model.addAttribute("pagination", pagination);
            return "view/forum";
        }

        for (int i = startIndex; i < pageCheck; i++){
            Forum forum = forumList.get(i);

            forumList2.add(forum);

            if(i == (totalListCnt-1)){
                pageCheck = totalListCnt-1;
            }

        }

        log.info(String.valueOf(startIndex));
        model.addAttribute(member);
        model.addAttribute("forumList", forumList2);
        model.addAttribute("pagination", pagination);

        model.addAttribute(member);
        return "view/forum";
    }

    @GetMapping("/forum_upload")
    public String uploadQuestion(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/forum_upload";
    }

    @PostMapping("/forum_upload")
    public String addQuestion(@CurrentUser Member member, Model model, @RequestParam String title,
                              @RequestParam String questionType, @RequestParam(required = false) String content,
                              @RequestParam(required = false) String content2, @RequestParam(required = false) String content3,
                              @RequestParam String question, @RequestParam String exampleA,
                              @RequestParam String exampleB, @RequestParam String exampleC,
                              @RequestParam String exampleD, @RequestParam String answer,
                              @RequestParam String solution) {


        Question q = questionService.createQuestion(questionType, content, content2, content3, question, exampleA, exampleB,
                exampleC, exampleD, answer, solution);

        forumService.addForum(member, title, q);
        model.addAttribute("member", member);
        return "redirect:/forum";
    }

    @GetMapping("/forumDetail/{id}")
    public String forumDetailView(@CurrentUser Member member, @PathVariable Long id, Model model) {
        log.info("상세보기");
        log.info(String.valueOf(id));
        Forum forum = forumRepository.findById(id).orElse(null);

        long hit = forum.getHit();
        hit++;
        forum.setHit(hit);
        forumRepository.save(forum);
        log.info("조회수 증가 성공!");

        Question question = questionRepository.findById(forum.getQuestionId()).orElse(null);
        assert question != null;
        QuestionType type = question.getQuestionType();
        if (type==QuestionType.PART1||type==QuestionType.PART2||type==QuestionType.PART3||type==QuestionType.PART4) {
            model.addAttribute("content",((LC) question).getContent());
            model.addAttribute("recording",((LC) question).getRecording());
            model.addAttribute("exampleA", ((LC) question).getExampleA());
            model.addAttribute("exampleB", ((LC) question).getExampleB());
            model.addAttribute("exampleC", ((LC) question).getExampleC());
            model.addAttribute("exampleD", ((LC) question).getExampleD());
            model.addAttribute("solution", ((LC) question).getSolution());
        }
        else {
            model.addAttribute("content", ((RC) question).getContent());
            model.addAttribute("content2", ((RC) question).getContent2());
            model.addAttribute("content3", ((RC) question).getContent3());
            model.addAttribute("exampleA", ((RC) question).getExampleA());
            model.addAttribute("exampleB", ((RC) question).getExampleB());
            model.addAttribute("exampleC", ((RC) question).getExampleC());
            model.addAttribute("exampleD", ((RC) question).getExampleD());
            model.addAttribute("solution", ((RC) question).getSolution());

        }
        model.addAttribute("forum", forum);
        model.addAttribute("member", member);
        model.addAttribute("question", question);
        model.addAttribute("type", question.getQuestionType());
        model.addAttribute("answer", question.getAnswer());
        model.addAttribute("questionExplanation", question.getQuestionExplanation());


        return "view/forum_detail";
    }

    @GetMapping("/vocabulary_test")
    public String vocabularyTest(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/vocabulary_test";
    }

    @GetMapping("/popup_dictionary")
    public String popupLayout(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);
        return "view/popup_dictionary";
    }

    @RequestMapping("/popup_dictionary_find/{word}")
    @ResponseBody
    public String dictionary(@PathVariable String word) throws Exception {
        String result;
        System.out.println(word);
        String[] splittedWord = word.split("");
        if (Pattern.matches("[a-zA-Z]", splittedWord[0])) {
            log.info("영어 -> 한국어");
            String loweredWord = word.toLowerCase();
            String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + loweredWord + "뜻";
            Document document = Jsoup.connect(url).get();
            Elements element = document.getElementsByAttributeValue("class", "mean api_txt_lines");
            String cut = element.get(0).text();
//            int index = cut.indexOf("(");
//            result = cut.substring(0, index);
            result = cut;

            return "{" + "\"" + "word" + "\"" + ":" + "\"" + word + "\"" + "," + "\"" + "meaning" + "\"" + ":" + "\"" + result + "\"" + "}";

        } else {
            log.info("한국어 -> 영어");
            String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=" + word + "영어로";
            Document document = Jsoup.connect(url).get();
            Elements element = document.getElementsByAttributeValue("class", "mean api_txt_lines");
            log.info(element.get(0).text());
            String cut = element.get(0).text();
            result = cut;

            return "{" + "\"" + "word" + "\"" + ":" + "\"" + result + "\"" + "," + "\"" + "meaning" + "\"" + ":" + "\"" + word + "\"" + "}";
        }

    }

    @RequestMapping("/add_word_list")
    @ResponseBody
    public String addWordList(@CurrentUser Member member, @RequestParam("word") String word, @RequestParam("meaning") String meaning) {
//    public String addWordList (Member member, HttpServletRequest request){
        String w = word.replaceAll("\"", " ").trim();
        String m = meaning.replaceAll("\"", " ").trim();
        JsonObject jsonObject = new JsonObject();
        boolean result = false;
//        String word = request.getParameter("word");
//        String meaning = request.getParameter("meaning");
        System.out.println(w);
        System.out.println(m);

        try {
            result = memberService.addWordList(member, w, m);
            if (result) {
                jsonObject.addProperty("message", "단어장에 추가");
            } else {
                jsonObject.addProperty("message", "단어장에 이미 존재합니다.");
            }

        } catch (IllegalArgumentException e) {
            jsonObject.addProperty("message", "잘못된정보");
        }

        return jsonObject.toString();
    }


    @GetMapping("/my_vocabulary_list")
    public String myVocabularyList(@CurrentUser Member member, Model model) {
        Word wordList = wordRepository.findByMember(member);
        System.out.println(wordList);
        if (wordList == null) {
            wordList = new Word();
            model.addAttribute("exist", "noWordList");
        }
        Map<String, String> map = wordList.getWord();

        model.addAttribute("wordList", map);

        model.addAttribute(member);

        return "view/my_vocabulary_list";
    }

    @GetMapping("/delete_word/{word}")
    public String DeleteTest(@CurrentUser Member member, Model model, @PathVariable String word) {
        memberService.deleteWord(member, word);
        System.out.println(word + "삭제");

        model.addAttribute("member", member);
        return "redirect:/my_vocabulary_list";
    }

    @RequestMapping(value="/upload_img", method = RequestMethod.POST)
    @ResponseBody
    public StringBuilder upload(@CurrentUser Member member, Model model, @RequestParam("file") MultipartFile file) throws IOException, IllegalStateException{
        StringBuilder loadText = new StringBuilder();
        FileOutputStream fos = null;
        System.out.println("Temp Path:" + System.getProperty("java.io.tmpdir"));
        System.out.println(file);
        try {
            byte fileData[] = file.getBytes();
            fos = new FileOutputStream(System.getProperty("java.io.tmpdir")+file.getName()+".jpg");
            fos.write(fileData);

        } finally {
            if (fos!= null){
                try {
                    fos.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        visionService.readText(file.getName(), loadText);

        model.addAttribute(member);
        return loadText;
    }
}
