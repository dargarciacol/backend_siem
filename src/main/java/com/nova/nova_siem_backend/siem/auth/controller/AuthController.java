package com.nova.nova_siem_backend.siem.auth.controller;

import com.nova.nova_siem_backend.siem.auth.dto.AuthResponse;
import com.nova.nova_siem_backend.siem.auth.dto.LoginRequest;
import com.nova.nova_siem_backend.siem.auth.service.AuthService;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequest request
    ) {
        AuthResponse user = authService.authenticate(request);

        String token = jwtService.generateToken(user);

        Map<String, Object> authData = new HashMap<>();
        authData.put("token", token);
        authData.put("user", user);

        return ResponseEntity.ok(
                ApiResponse.success("Authentication successful", authData)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully", null)
        );
    }
}