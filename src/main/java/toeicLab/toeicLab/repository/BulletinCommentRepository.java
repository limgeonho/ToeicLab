package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.BulletinComment;

import java.util.List;

@Repository
public interface BulletinCommentRepository extends JpaRepository<BulletinComment, Long> {

    List<BulletinComment> findAllByBulletinId(Long id);
}
