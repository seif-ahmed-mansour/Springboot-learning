package com.demo.crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

/**
 * Central Spring Security configuration.
 *
 * - Registers PasswordEncoder and AuthenticationManager beans.
 * - Defines a SecurityFilterChain (the core HTTP security policy).
 * - Optionally inserts a JwtAuthenticationFilter if one is available in the context.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsService userDetailsService;

    /**
     * The main HTTP security configuration (filter chain).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 1) Disable CSRF for stateless APIs (enable only for browser-based forms)
        http.csrf(AbstractHttpConfigurer::disable);

        // 2) Exception handling: if an AuthenticationEntryPoint bean exists use it, otherwise return 401
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
        );

        // 3) Make session stateless (we rely on JWTs, not HTTP session)
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4) Authorization rules: public endpoints and authenticated endpoints
        http.authorizeHttpRequests(auth -> auth
                // public endpoints that do not require authentication
                .requestMatchers(
                        "/auth/**", "/login",
                        "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**", "/error"
                )
                .permitAll()
                // everything else requires authentication
                .anyRequest().authenticated()
        );

        // Set authentication provider
        http.authenticationProvider(authenticationProvider());

        // 6) If a JwtAuthenticationFilter is available, add it before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // 7) Build and return the composed filter chain
        return http.build();
    }

    //TODO: use another authentication provide not Dao because it is deprecated
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * AuthenticationManager bean so you can inject it into controllers/services (e.g., for login).
     * This delegates to Spring's AuthenticationConfiguration so that the framework builds the manager
     * using available AuthenticationProviders (DAO provider using your UserDetailsService and PasswordEncoder).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Password encoder bean. Use BCrypt for hashing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
