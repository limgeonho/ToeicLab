package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Bulletin;
@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
    Bulletin findByWriterId(String writerId);
}
