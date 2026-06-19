package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.games")
    List<Member> findAllWithGames();

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.games WHERE m.isActive = true")
    List<Member> findByIsActiveTrue();

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.games WHERE m.isActive = false")
    List<Member> findByIsActiveFalse();

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.games WHERE m.id = :id")
    Optional<Member> findByIdWithGames(Long id);
}