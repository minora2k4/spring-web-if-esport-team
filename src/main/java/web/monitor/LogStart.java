package web.monitor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogStart {

    private static final Logger log = LoggerFactory.getLogger(LogStart.class);
    private final Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void onStarted() {
        log.info("================================================");
        log.info("       IF TEAM WEB — APPLICATION STARTED        ");
        log.info("================================================");
        log.info("  Port    : {}", env.getProperty("server.port", "8081"));
        log.info("  DB      : {}", env.getProperty("spring.datasource.url"));
        log.info("  Profile : {}", String.join(", ",
                env.getActiveProfiles().length > 0
                        ? env.getActiveProfiles()
                        : new String[]{"default"}));
        log.info("================================================");
    }
}