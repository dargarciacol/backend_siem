package com.nova.nova_siem_backend.siem.auth.controller;

import com.nova.nova_siem_backend.siem.auth.dto.LoginRequest;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid credentials"));
        }

        String mockJwt = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();

        Cookie authCookie = new Cookie("NOVA_SESSION", mockJwt);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(authCookie);

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", request.getUsername());
        userData.put("role", "SOC_L2");
        userData.put("fullName", "Santiago Garcia");

        return ResponseEntity.ok(ApiResponse.success("Authentication successful", userData));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        Cookie authCookie = new Cookie("NOVA_SESSION", null);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(0);

        response.addCookie(authCookie);

        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}