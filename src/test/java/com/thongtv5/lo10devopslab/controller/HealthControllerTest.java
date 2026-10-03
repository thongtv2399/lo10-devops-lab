package com.thongtv5.lo10devopslab.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Kiểm tra endpoint Health của ứng dụng.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

    private final MockMvc mockMvc;

    /**
     * Inject MockMvc dùng để gửi HTTP request mô phỏng.
     *
     * @param mockMvc MockMvc đã được Spring cấu hình
     */
    @Autowired
    HealthControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("GET /api/health should return application status")
    void shouldReturnApplicationHealth()
            throws Exception {

        mockMvc.perform(
                get("/api/health")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                )
        )
        .andExpect(
                jsonPath("$.status").value("UP")
        )
        .andExpect(
                jsonPath("$.service")
                        .value("lo10-devops-lab")
        )
        .andExpect(
                jsonPath("$.timestamp").exists()
        )
        .andExpect(
                jsonPath("$.timestamp").isNotEmpty()
        );
    }

    @Test
    @DisplayName("Unknown endpoint should return 404")
    void shouldReturnNotFoundForUnknownEndpoint()
            throws Exception {

        mockMvc.perform(
                get("/api/unknown")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(
                status().isNotFound()
        );
    }
}