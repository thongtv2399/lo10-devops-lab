package com.thongtv5.lo10devopslab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Chứa dữ liệu Client gửi lên khi tạo message.
public record CreateMessageRequest(

        @NotBlank(message = "Content must not be blank")
        @Size(
                max = 500,
                message = "Content must not exceed 500 characters"
        )
        String content
) {
}