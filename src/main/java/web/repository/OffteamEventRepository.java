package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.OffteamEvent;
import java.util.List;
import java.util.Optional;

public interface OffteamEventRepository extends JpaRepository<OffteamEvent, Long> {

    @Query("SELECT e FROM OffteamEvent e LEFT JOIN FETCH e.photos ORDER BY e.eventDate DESC")
    List<OffteamEvent> findAllOrderByDate();

    @Query("SELECT e FROM OffteamEvent e LEFT JOIN FETCH e.photos WHERE e.id = :id")
    Optional<OffteamEvent> findByIdWithPhotos(Long id);
}