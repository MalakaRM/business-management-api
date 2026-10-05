package com.smartbusiness.businessmanagement.dto.response;

import java.util.Set;

public record LoginResponse(
        String token,
        String tokenType,
        String username,
        Set<String> roles,
        Set<String> permissions,
        boolean passwordChangeRequired
) {
}