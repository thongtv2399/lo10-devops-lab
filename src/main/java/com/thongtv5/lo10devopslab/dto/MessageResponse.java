package com.thongtv5.lo10devopslab.dto;

import java.time.Instant;

/**
 * Chứa dữ liệu trả về cho Client sau khi tạo hoặc lấy message.
 *
 * @param id ID duy nhất của message
 * @param content nội dung message
 * @param createdAt thời điểm message được tạo
 */
public record MessageResponse(
        Long id,
        String content,
        Instant createdAt
) {
}