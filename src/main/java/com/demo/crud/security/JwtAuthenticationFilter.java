package com.demo.crud.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json");

        String path = request.getServletPath();
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            sendErrorToClient(response, "No JWT token found in the header", HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtService.isValidToken(token)) {
            sendErrorToClient(response, "Invalid or expired token", HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // Valid token, load user details
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null || userDetails.getUsername().isBlank()) {
            sendErrorToClient(response, "User not found", HttpStatus.NOT_FOUND.value());
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/") ||
                path.equals("/login") ||
                path.startsWith("/actuator/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/h2-console/");
    }

    private void sendErrorToClient(HttpServletResponse response, String message, int status) throws IOException {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("error", "Unauthorized");
        responseMap.put("status", String.valueOf(status));
        responseMap.put("message", message);
        responseMap.put("timestamp", LocalDateTime.now().toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        objectMapper.writeValue(response.getOutputStream(), responseMap);
    }
}