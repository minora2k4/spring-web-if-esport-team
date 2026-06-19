package web.monitor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class LogDB {

    private static final Logger log = LoggerFactory.getLogger(LogDB.class);
    private final DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void checkDB() {
        try (Connection conn = dataSource.getConnection()) {
            log.info("[DB] >>> Connected — {} v{}",
                    conn.getMetaData().getDatabaseProductName(),
                    conn.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            log.error("[DB] >>> Failed — {}", e.getMessage());
        }
    }
}