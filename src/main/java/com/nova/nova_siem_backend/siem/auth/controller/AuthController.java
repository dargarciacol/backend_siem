package com.nova.nova_siem_backend.siem.auth.controller;

import com.nova.nova_siem_backend.siem.auth.dto.AuthResponse;
import com.nova.nova_siem_backend.siem.auth.dto.LoginRequest;
import com.nova.nova_siem_backend.siem.auth.service.AuthService;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse user = authService.authenticate(request);
        String token = jwtService.generateToken(user);

        // Cumpliendo el requerimiento SOC: Establecer cookie segura NOVA_SESSION
        Cookie sessionCookie = new Cookie("NOVA_SESSION", token);
        sessionCookie.setHttpOnly(true); // Evita lectura desde JavaScript (Anti-XSS)
        sessionCookie.setSecure(true);   // Obliga uso de HTTPS (Render/Netlify)
        sessionCookie.setPath("/");      // Disponible en todos los endpoints de la API
        sessionCookie.setMaxAge(86400);  // 24 horas (En segundos)
        response.addCookie(sessionCookie);

        // Mantenemos el token en el body (authData) para NO romper el frontend actual
        Map<String, Object> authData = new HashMap<>();
        authData.put("token", token);
        authData.put("user", user);

        return ResponseEntity.ok(
                ApiResponse.success("Authentication successful", authData)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        // Destruir la cookie en el cliente al cerrar sesión
        Cookie sessionCookie = new Cookie("NOVA_SESSION", null);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0); // Elimina la cookie
        response.addCookie(sessionCookie);

        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully", null)
        );
    }
}