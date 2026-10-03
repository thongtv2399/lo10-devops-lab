package com.thongtv5.lo10devopslab.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.thongtv5.lo10devopslab.dto.CreateMessageRequest;
import com.thongtv5.lo10devopslab.dto.MessageResponse;
import com.thongtv5.lo10devopslab.service.MessageService;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @Autowired
    MessageControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("POST /api/messages should create message")
    void shouldCreateMessage() throws Exception {

        MessageResponse createdMessage =
                new MessageResponse(
                        1L,
                        "Docker and CI/CD are ready",
                        Instant.parse(
                                "2026-10-03T12:30:00Z"
                        )
                );

        when(
                messageService.createMessage(
                        any(CreateMessageRequest.class)
                )
        ).thenReturn(createdMessage);

        String requestBody =
                """
                {
                  "content": "Docker and CI/CD are ready"
                }
                """;

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(
                status().isCreated()
        )
        .andExpect(
                header().string(
                        "Location",
                        "/api/messages/1"
                )
        )
        .andExpect(
                jsonPath("$.id").value(1)
        )
        .andExpect(
                jsonPath("$.content")
                        .value(
                                "Docker and CI/CD are ready"
                        )
        )
        .andExpect(
                jsonPath("$.createdAt")
                        .value(
                                "2026-10-03T12:30:00Z"
                        )
        );
    }

    @Test
    @DisplayName("POST /api/messages should reject blank content")
    void shouldRejectBlankContent() throws Exception {

        String requestBody =
                """
                {
                  "content": "   "
                }
                """;

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("POST /api/messages should reject missing content")
    void shouldRejectMissingContent() throws Exception {

        String requestBody =
                """
                {
                }
                """;

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("GET /api/messages/{id} should return message")
    void shouldReturnExistingMessage() throws Exception {

        MessageResponse existingMessage =
                new MessageResponse(
                        10L,
                        "Structured Logging configured",
                        Instant.parse(
                                "2026-10-03T13:00:00Z"
                        )
                );

        when(
                messageService.findById(10L)
        ).thenReturn(
                Optional.of(existingMessage)
        );

        mockMvc.perform(
                get("/api/messages/10")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(
                status().isOk()
        )
        .andExpect(
                jsonPath("$.id").value(10)
        )
        .andExpect(
                jsonPath("$.content")
                        .value(
                                "Structured Logging configured"
                        )
        )
        .andExpect(
                jsonPath("$.createdAt")
                        .value(
                                "2026-10-03T13:00:00Z"
                        )
        );
    }

    @Test
    @DisplayName("GET /api/messages/{id} should return 404")
    void shouldReturnNotFoundForUnknownMessage()
            throws Exception {

        when(
                messageService.findById(999L)
        ).thenReturn(
                Optional.empty()
        );

        mockMvc.perform(
                get("/api/messages/999")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(
                status().isNotFound()
        );
    }

    @Test
    @DisplayName("GET /api/messages/{id} should reject invalid ID")
    void shouldRejectInvalidMessageId()
            throws Exception {

        mockMvc.perform(
                get("/api/messages/not-a-number")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(
                status().isBadRequest()
        );
    }
}