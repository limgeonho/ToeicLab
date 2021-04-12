package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Question;
import toeicLab.toeicLab.domain.QuestionType;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByQuestionType(QuestionType questionType);

    List<Question> findAllBySmallSetId(int smallSetId);

    List<Question> findAllByQuestionTypeAndSmallSetType(QuestionType questionType, int smallSetType);

}
