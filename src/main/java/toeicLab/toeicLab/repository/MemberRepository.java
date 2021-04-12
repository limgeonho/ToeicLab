package toeicLab.toeicLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toeicLab.toeicLab.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

    boolean existsByContact(String contact);

    Member findByUserId(String userId);

    Member findByUsername(String username);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
}
