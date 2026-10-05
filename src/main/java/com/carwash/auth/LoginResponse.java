package com.carwash.auth;

public record LoginResponse(
        Long id,
        String name,
        String email,
        String role,
        String token
) {
}
