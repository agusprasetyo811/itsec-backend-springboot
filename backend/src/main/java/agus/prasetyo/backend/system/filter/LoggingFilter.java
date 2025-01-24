package agus.prasetyo.backend.system.filter;

import agus.prasetyo.backend.apps.model.dto.LoggerDTO;
import agus.prasetyo.backend.apps.model.request.LoggerRequest;
import agus.prasetyo.backend.system.service.DeviceDetectService;
import agus.prasetyo.backend.apps.service.LoggerService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private final LoggerService loggerService;

    public LoggingFilter(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        DeviceDetectService deviceDetectService = new DeviceDetectService();


        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Log request and response bodies
            String action = wrappedRequest.getRequestURI();
            String method = wrappedRequest.getMethod();
            String requestBody = logRequestBody(wrappedRequest);
            String responseBody = logResponseBody(wrappedResponse);
            String userAgent = wrappedRequest.getHeader("User-Agent");
            String status = String.valueOf(wrappedResponse.getStatus());
            String ipAddress = wrappedRequest.getRemoteAddr();

            logger.info("URL: {}", action);
            logger.info("Method: {}", method);
            logger.info("UserAgent: {}", userAgent);
            logger.info("Device Type: {}", deviceDetectService.detect(userAgent));
            logger.info("Get IP: {}", ipAddress);
            logger.info("Status: {}", status);
            logger.info("Request Body: {}", requestBody);
            logger.info("Response Body: {}", responseBody);
            wrappedResponse.copyBodyToResponse();
            loggerService.create(new LoggerRequest(action, status, requestBody, responseBody, userAgent, ipAddress,  deviceDetectService.detect(userAgent)));
        }
    }

    private String logRequestBody(ContentCachingRequestWrapper request) throws UnsupportedEncodingException {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            return new String(buf, 0, buf.length, request.getCharacterEncoding());
        }
        return "";
    }

    private String logResponseBody(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            return new String(buf, 0, buf.length, response.getCharacterEncoding());
        }
        return "";
    }
}
