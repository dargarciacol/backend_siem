package com.nova.nova_siem_backend.siem.auth.service;

import com.nova.nova_siem_backend.siem.auth.dto.AuthResponse;
import com.nova.nova_siem_backend.siem.auth.dto.LoginRequest;
import com.nova.nova_siem_backend.siem.auth.model.User;
import com.nova.nova_siem_backend.siem.auth.repository.UserRepository;
import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.exception.InvalidCredentialsException;
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

    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new BusinessException("Username and password are required.");
        }

        User user = userRepository.findByUsername(request.getUsername().trim())
                .filter(User::isActive)
                .filter(candidate -> passwordEncoder.matches(request.getPassword(), candidate.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password."));

        return new AuthResponse(user.getUsername(), user.getFullName(), user.getRole().name());
    }
}