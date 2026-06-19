package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.TeamLog;
import java.util.List;

public interface TeamLogRepository extends JpaRepository<TeamLog, Long> {

    @Query("SELECT t FROM TeamLog t LEFT JOIN FETCH t.member ORDER BY t.eventDate DESC")
    List<TeamLog> findAllWithMember();
}