package toeicLab.toeicLab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toeicLab.toeicLab.domain.Forum;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.domain.Question;
import toeicLab.toeicLab.repository.ForumRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ForumService {
    private final ForumRepository forumRepository;

    public void addForum(Member member, String title, Question q) {
        Forum forum = Forum.builder()
                .title(title)
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .date(LocalDateTime.now())
                .hit(0L)
                .questionId(q.getId())
                .build();
        forumRepository.save(forum);


    }
}
