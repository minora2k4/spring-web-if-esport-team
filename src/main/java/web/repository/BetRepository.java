package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.Bet;
import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    @Query("SELECT b FROM Bet b LEFT JOIN FETCH b.game ORDER BY b.betDate DESC")
    List<Bet> findAllWithGame();
}