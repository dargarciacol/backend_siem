
package com.nova.nova_siem_backend.siem.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // =========================================================
    // EXCLUDE CORS PREFLIGHT REQUESTS
    // =========================================================

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    // =========================================================
    // JWT AUTHENTICATION
    // =========================================================

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // No existe token: continuar con la cadena de filtros.
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {

            // Extraer usuario desde el JWT.
            String username = jwtService.extractUsername(token);

            if (
                    username != null &&
                            SecurityContextHolder.getContext()
                                    .getAuthentication() == null
            ) {

                // Cargar información del usuario.
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Verificar que la cuenta esté habilitada.
                if (userDetails.isEnabled()) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (
                JwtException |
                IllegalArgumentException |
                UsernameNotFoundException exception
        ) {

            // Limpiar autenticación si el token no es válido.
            SecurityContextHolder.clearContext();
        }

        // Continuar con la cadena de filtros.
        filterChain.doFilter(request, response);
    }
}
