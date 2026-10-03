package com.thongtv5.lo10devopslab.logging;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.thongtv5.lo10devopslab.controller.HealthController;

@WebMvcTest(HealthController.class)
@Import(CorrelationIdFilter.class)
class CorrelationIdFilterTest {

    private static final String UUID_PATTERN =
            "^[0-9a-fA-F]{8}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{12}$";

    private final MockMvc mockMvc;

    @Autowired
    CorrelationIdFilterTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Should preserve incoming correlation ID")
    void shouldPreserveIncomingCorrelationId()
            throws Exception {

        mockMvc.perform(
                get("/api/health")
                        .header(
                                CorrelationIdFilter.CORRELATION_ID_HEADER,
                                "lo10-test-001"
                        )
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                header().string(
                        CorrelationIdFilter.CORRELATION_ID_HEADER,
                        "lo10-test-001"
                )
        )
        .andExpect(
                jsonPath("$.status").value("UP")
        );
    }

    @Test
    @DisplayName("Should generate correlation ID when header is missing")
    void shouldGenerateCorrelationIdWhenHeaderIsMissing()
            throws Exception {

        mockMvc.perform(
                get("/api/health")
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                header().exists(
                        CorrelationIdFilter.CORRELATION_ID_HEADER
                )
        )
        .andExpect(
                header().string(
                        CorrelationIdFilter.CORRELATION_ID_HEADER,
                        matchesPattern(UUID_PATTERN)
                )
        );
    }

    @Test
    @DisplayName("Should generate correlation ID for blank header")
    void shouldGenerateCorrelationIdForBlankHeader()
            throws Exception {

        mockMvc.perform(
                get("/api/health")
                        .header(
                                CorrelationIdFilter.CORRELATION_ID_HEADER,
                                "   "
                        )
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                header().string(
                        CorrelationIdFilter.CORRELATION_ID_HEADER,
                        matchesPattern(UUID_PATTERN)
                )
        );
    }

    @Test
    @DisplayName("Should trim incoming correlation ID")
    void shouldTrimIncomingCorrelationId()
            throws Exception {

        mockMvc.perform(
                get("/api/health")
                        .header(
                                CorrelationIdFilter.CORRELATION_ID_HEADER,
                                "  lo10-test-002  "
                        )
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                header().string(
                        CorrelationIdFilter.CORRELATION_ID_HEADER,
                        "lo10-test-002"
                )
        );
    }
}