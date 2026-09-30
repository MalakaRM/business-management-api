package com.smartbusiness.businessmanagement.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        boolean success,
        String message,
        String path,
        LocalDateTime timestamp
) {
}