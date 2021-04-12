package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.domain.ReviewNote;

@Repository
public interface ReviewNoteRepository extends JpaRepository<ReviewNote, Long> {

    ReviewNote findByMember(Member member);

}
