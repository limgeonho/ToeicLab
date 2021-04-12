package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.domain.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Word findByMember(Member member);

}
