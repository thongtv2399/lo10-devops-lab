package com.thongtv5.lo10devopslab.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thongtv5.lo10devopslab.dto.CreateMessageRequest;
import com.thongtv5.lo10devopslab.dto.MessageResponse;
import com.thongtv5.lo10devopslab.service.MessageService;

import jakarta.validation.Valid;

// Cung cấp API tạo và lấy Message.
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    // Inject MessageService qua constructor.
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Tạo Message mới và trả về HTTP 201 Created.
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(
            @Valid @RequestBody CreateMessageRequest request) {

        MessageResponse createdMessage =
                messageService.createMessage(request);

        URI location = URI.create(
                "/api/messages/" + createdMessage.id()
        );

        return ResponseEntity
                .created(location)
                .body(createdMessage);
    }

    // Lấy Message theo ID.
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(
            @PathVariable Long messageId) {

        return messageService
                .findById(messageId)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }
}