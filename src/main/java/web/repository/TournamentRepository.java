package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.Tournament;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query("SELECT t FROM Tournament t LEFT JOIN FETCH t.game ORDER BY t.startDate DESC")
    List<Tournament> findAllWithGame();
}