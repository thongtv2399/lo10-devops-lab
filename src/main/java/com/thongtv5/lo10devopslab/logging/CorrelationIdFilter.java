package com.thongtv5.lo10devopslab.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Gắn Correlation ID vào từng HTTP request và JSON log.
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER =
            "X-Correlation-ID";

    public static final String CORRELATION_ID_MDC_KEY =
            "correlationId";

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    CorrelationIdFilter.class
            );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId =
                resolveCorrelationId(request);

        MDC.put(
                CORRELATION_ID_MDC_KEY,
                correlationId
        );

        response.setHeader(
                CORRELATION_ID_HEADER,
                correlationId
        );

        try {
            LOGGER.info(
                    "HTTP request started: method={}, path={}",
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(
                    request,
                    response
            );

            LOGGER.info(
                    "HTTP request completed: method={}, path={}, status={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus()
            );
        } finally {
            MDC.remove(
                    CORRELATION_ID_MDC_KEY
            );
        }
    }

    private String resolveCorrelationId(
            HttpServletRequest request) {

        String incomingCorrelationId =
                request.getHeader(
                        CORRELATION_ID_HEADER
                );

        if (incomingCorrelationId == null
                || incomingCorrelationId.isBlank()) {

            return UUID.randomUUID()
                    .toString();
        }

        return incomingCorrelationId.trim();
    }
}