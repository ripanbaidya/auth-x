package org.astrobrains.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for authentication-related beans.
 * Provides AuthenticationProvider and PasswordEncoder for Spring Security.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Configures the AuthenticationProvider (DAO based).
     * Uses UserDetailsService for fetching users and BCrypt for password encoding.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        log.info("AuthenticationProvider bean initialized with DaoAuthenticationProvider");
        return authenticationProvider;
    }

    /**
     * Configures the PasswordEncoder.
     * BCrypt is a strong hashing function resistant to rainbow table attacks.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("PasswordEncoder bean initialized with BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}
