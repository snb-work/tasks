package com.example.tasks.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Skip logging for actuator endpoints
        if (request.getRequestURI().startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap request and response to allow reading the body multiple times
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Record start time to calculate request duration
        long startTime = System.currentTimeMillis();
        
        try {
            // Process the request
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Always log the request/response, even if an exception occurs
            long duration = System.currentTimeMillis() - startTime;
            logRequestAndResponse(wrappedRequest, wrappedResponse, duration);
            
            // Important: Copy the response back to the original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper request,
                                     ContentCachingResponseWrapper response,
                                     long durationMs) throws IOException {
        
        // Log basic request info
        log.info("\n=== Request ===\n" +
                "{} {} {}\n" +
                "From: {}\n" +
                "Content-Type: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() != null ? "?" + request.getQueryString() : "",
                request.getRemoteAddr(),
                request.getContentType());

        // Log request body if present
        String requestBody = new String(request.getContentAsByteArray());
        if (!requestBody.isBlank()) {
            log.info("Request body: {}", formatJsonIfPossible(requestBody));
        }

        // Log response info
        log.info("\n=== Response ({} ms) ===\n" +
                "Status: {}\n" +
                "Content-Type: {}",
                durationMs,
                response.getStatus(),
                response.getContentType());

        // Log response body if present
        String responseBody = new String(response.getContentAsByteArray());
        if (!responseBody.isBlank()) {
            log.info("Response body: {}", formatJsonIfPossible(responseBody));
        }
    }

    /**
     * Pretty-print JSON if the input is valid JSON, otherwise return as-is
     */
    private String formatJsonIfPossible(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter()
                             .writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json; // Return original if not valid JSON
        }
    }
}
