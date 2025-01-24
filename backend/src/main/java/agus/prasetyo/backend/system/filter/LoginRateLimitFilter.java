package agus.prasetyo.backend.system.filter;

import agus.prasetyo.backend.system.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class LoginRateLimitFilter implements Filter {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().equals("/api/auth/login")) {
            String clientIp = request.getRemoteAddr();

            if (rateLimitService.isBlocked(clientIp)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Your IP is blocked. Please try again later.");
                return;
            }
            Bucket bucket = rateLimitService.resolveBucket(clientIp);

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response);
            } else {
                rateLimitService.blockClient(clientIp);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Too many login attempts. Your IP is now blocked.");
                return;
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}

