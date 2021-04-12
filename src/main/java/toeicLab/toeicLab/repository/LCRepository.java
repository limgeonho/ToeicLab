package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toeicLab.toeicLab.domain.LC;

public interface LCRepository extends JpaRepository<LC, Long> {
}
