package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.domain.QuestionSet;

import java.util.List;

@Repository
public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long> {
    List<QuestionSet> getAllByMember(Member member);
}
