package com.thongtv5.lo10devopslab.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thongtv5.lo10devopslab.dto.CreateMessageRequest;
import com.thongtv5.lo10devopslab.dto.MessageResponse;

@Service
public class MessageService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MessageService.class);

    private final AtomicLong idGenerator =
            new AtomicLong();

    private final Map<Long, MessageResponse> messages =
            new ConcurrentHashMap<>();

    public MessageResponse createMessage(
            CreateMessageRequest request) {

        long messageId =
                idGenerator.incrementAndGet();

        MessageResponse message =
                new MessageResponse(
                        messageId,
                        request.content().trim(),
                        Instant.now()
                );

        messages.put(
                messageId,
                message
        );

        LOGGER.atInfo()
                .addKeyValue(
                        "messageId",
                        messageId
                )
                .addKeyValue(
                        "operation",
                        "createMessage"
                )
                .log(
                        "Message created successfully"
                );

        return message;
    }

    public Optional<MessageResponse> findById(
            Long messageId) {

        if (messageId == null) {
            LOGGER.atWarn()
                    .addKeyValue(
                            "operation",
                            "findMessage"
                    )
                    .log(
                            "Message ID was null"
                    );

            return Optional.empty();
        }

        Optional<MessageResponse> result =
                Optional.ofNullable(
                        messages.get(messageId)
                );

        if (result.isPresent()) {
            LOGGER.atInfo()
                    .addKeyValue(
                            "messageId",
                            messageId
                    )
                    .addKeyValue(
                            "operation",
                            "findMessage"
                    )
                    .log(
                            "Message found"
                    );
        } else {
            LOGGER.atWarn()
                    .addKeyValue(
                            "messageId",
                            messageId
                    )
                    .addKeyValue(
                            "operation",
                            "findMessage"
                    )
                    .log(
                            "Message was not found"
                    );
        }

        return result;
    }

    public int countMessages() {
        return messages.size();
    }

    public void clearMessages() {

        int removedMessageCount =
                messages.size();

        messages.clear();
        idGenerator.set(0);

        LOGGER.atInfo()
                .addKeyValue(
                        "removedMessageCount",
                        removedMessageCount
                )
                .addKeyValue(
                        "operation",
                        "clearMessages"
                )
                .log(
                        "All messages were cleared"
                );
    }
}