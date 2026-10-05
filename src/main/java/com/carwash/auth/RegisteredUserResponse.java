package com.carwash.auth;

import com.carwash.user.User;
import com.carwash.user.UserRole;

public record RegisteredUserResponse(Long id, String name, String phone, String email, UserRole role) {
    public static RegisteredUserResponse from(User user) {
        return new RegisteredUserResponse(user.getId(), user.getName(), user.getPhone(), user.getEmail(), user.getRole());
    }
}
