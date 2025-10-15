package com.demo.crud.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        System.out.println("=== AuthenticationEntryPoint Called ===");
        System.out.println("Request Path: " + request.getServletPath());
        System.out.println("Exception: " + authException.getMessage());
        System.out.println("Exception Type: " + authException.getClass().getName());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("error", "Unauthorized");
        responseMap.put("message", "Authorization failed, include a valid Bearer token in the header");
        responseMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        responseMap.put("path", request.getServletPath());

        objectMapper.writeValue(response.getOutputStream(), responseMap);
    }
}
