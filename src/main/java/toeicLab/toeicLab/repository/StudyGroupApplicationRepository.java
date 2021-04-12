package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.StudyGroupApplication;

import java.util.List;

@Repository
public interface StudyGroupApplicationRepository extends JpaRepository<StudyGroupApplication, Long> {
    public List<StudyGroupApplication> findAll();
    public List<StudyGroupApplication> findAllByMatching(boolean matching);
}
