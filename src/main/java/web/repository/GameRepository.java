package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Game;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}