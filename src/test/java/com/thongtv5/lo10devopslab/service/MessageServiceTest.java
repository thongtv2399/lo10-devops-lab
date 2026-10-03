package com.thongtv5.lo10devopslab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thongtv5.lo10devopslab.dto.CreateMessageRequest;
import com.thongtv5.lo10devopslab.dto.MessageResponse;

/**
 * Kiểm tra logic quản lý Message trong memory.
 */
class MessageServiceTest {

    private MessageService messageService;

    /**
     * Tạo MessageService mới trước mỗi test.
     */
    @BeforeEach
    void setUp() {
        messageService = new MessageService();
    }

    @Test
    @DisplayName("Should create message successfully")
    void shouldCreateMessageSuccessfully() {

        CreateMessageRequest request =
                new CreateMessageRequest(
                        "Docker and CI/CD are ready"
                );

        MessageResponse response =
                messageService.createMessage(request);

        assertEquals(
                1L,
                response.id()
        );

        assertEquals(
                "Docker and CI/CD are ready",
                response.content()
        );

        assertNotNull(
                response.createdAt()
        );

        assertEquals(
                1,
                messageService.countMessages()
        );
    }

    @Test
    @DisplayName("Should trim message content")
    void shouldTrimMessageContent() {

        CreateMessageRequest request =
                new CreateMessageRequest(
                        "   Structured Logging   "
                );

        MessageResponse response =
                messageService.createMessage(request);

        assertEquals(
                "Structured Logging",
                response.content()
        );
    }

    @Test
    @DisplayName("Should generate increasing message IDs")
    void shouldGenerateIncreasingMessageIds() {

        MessageResponse firstMessage =
                messageService.createMessage(
                        new CreateMessageRequest(
                                "First message"
                        )
                );

        MessageResponse secondMessage =
                messageService.createMessage(
                        new CreateMessageRequest(
                                "Second message"
                        )
                );

        assertEquals(
                1L,
                firstMessage.id()
        );

        assertEquals(
                2L,
                secondMessage.id()
        );

        assertEquals(
                2,
                messageService.countMessages()
        );
    }

    @Test
    @DisplayName("Should find existing message by ID")
    void shouldFindExistingMessageById() {

        MessageResponse createdMessage =
                messageService.createMessage(
                        new CreateMessageRequest(
                                "Correlation ID configured"
                        )
                );

        Optional<MessageResponse> result =
                messageService.findById(
                        createdMessage.id()
                );

        assertTrue(
                result.isPresent()
        );

        assertEquals(
                createdMessage.id(),
                result.get().id()
        );

        assertEquals(
                "Correlation ID configured",
                result.get().content()
        );
    }

    @Test
    @DisplayName("Should return empty for unknown message ID")
    void shouldReturnEmptyForUnknownMessageId() {

        Optional<MessageResponse> result =
                messageService.findById(
                        999L
                );

        assertTrue(
                result.isEmpty()
        );
    }

    @Test
    @DisplayName("Should return empty for null message ID")
    void shouldReturnEmptyForNullMessageId() {

        Optional<MessageResponse> result =
                messageService.findById(
                        null
                );

        assertTrue(
                result.isEmpty()
        );
    }

    @Test
    @DisplayName("Should clear messages and reset ID generator")
    void shouldClearMessagesAndResetIdGenerator() {

        messageService.createMessage(
                new CreateMessageRequest(
                        "First message"
                )
        );

        messageService.createMessage(
                new CreateMessageRequest(
                        "Second message"
                )
        );

        assertEquals(
                2,
                messageService.countMessages()
        );

        messageService.clearMessages();

        assertEquals(
                0,
                messageService.countMessages()
        );

        assertFalse(
                messageService.findById(1L).isPresent()
        );

        MessageResponse newMessage =
                messageService.createMessage(
                        new CreateMessageRequest(
                                "Message after reset"
                        )
                );

        assertEquals(
                1L,
                newMessage.id()
        );
    }
}