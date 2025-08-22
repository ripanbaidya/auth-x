package org.astrobrains.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.astrobrains.config.JwtService;
import org.astrobrains.dto.request.AuthenticationRequest;
import org.astrobrains.dto.request.RegistrationRequest;
import org.astrobrains.dto.response.AuthenticationResponse;
import org.astrobrains.email.EmailTemplateName;
import org.astrobrains.model.Token;
import org.astrobrains.model.User;
import org.astrobrains.repository.RoleRepository;
import org.astrobrains.repository.TokenRepository;
import org.astrobrains.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(@Valid RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // TODO: better exception handling
                .orElseThrow(() -> new IllegalStateException("User Role not set"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activate your account"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String character = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i ++) {
            int randomIndex = secureRandom.nextInt(character.length());
            codeBuilder.append(character.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user: {}", request.getEmail());
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = (User)auth.getPrincipal();
        claims.put("fullName", user.getFullName());

        // this section might not work
        var jwtToken = jwtService.buildToken(claims, user, jwtService.getJwtExpiration());

        log.info("Authentication Successfull for user {}", user.getFullName());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    /**
     * Activate the user account
     * @param token
     */
//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // TODO: better exceptional hanlding
                .orElseThrow(() -> new IllegalStateException("Invalid token !!"));


        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            log.error("Token has expired on {}. Sending a new token to {}",
                    savedToken.getExpiredAt(), savedToken.getUser().getEmail());
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent.");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setExpiredAt(LocalDateTime.now()); // validated at
        tokenRepository.save(savedToken);
    }
}
