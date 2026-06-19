package web.monitor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogRequest implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogRequest.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            log.info("[REQUEST] {} {} → {} ({}ms)",
                    req.getMethod(),
                    req.getRequestURI(),
                    res.getStatus(),
                    System.currentTimeMillis() - start);
        }
    }
}