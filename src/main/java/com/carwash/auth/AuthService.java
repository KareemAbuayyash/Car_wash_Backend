package com.carwash.auth;

import com.carwash.user.User;
import com.carwash.user.UserRepository;
import com.carwash.user.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisteredUserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateRegistrationException("Email is already registered");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateRegistrationException("Phone is already registered");
        }

        User user = new User(request.name(), request.phone(), request.email(),
                passwordEncoder.encode(request.password()), UserRole.CUSTOMER);
        return RegisteredUserResponse.from(userRepository.save(user));
    }
}
