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
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name(), token);
    }
}
