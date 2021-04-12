package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.StudyGroup;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    StudyGroup findById(long id);
}
