package com.smartbusiness.businessmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        boolean success,
        String message,
        String path,
        Map<String, String> errors,
        LocalDateTime timestamp
) {
}