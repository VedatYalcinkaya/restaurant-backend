package com.demirciyazilim.webapi.config;

import com.demirciyazilim.business.rules.UserBusinessRules;
import com.demirciyazilim.entities.User;
import com.demirciyazilim.entities.enums.Role;
import com.demirciyazilim.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.enabled:false}")
    private boolean enabled;

    @Value("${app.bootstrap.admin.username:}")
    private String username;

    @Value("${app.bootstrap.admin.email:}")
    private String email;

    @Value("${app.bootstrap.admin.password:}")
    private String password;

    @Value("${app.bootstrap.admin.full-name:System Administrator}")
    private String fullName;

    public AdminBootstrapRunner(
            UserRepository userRepository,
            UserBusinessRules userBusinessRules,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userBusinessRules = userBusinessRules;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!enabled) {
            return;
        }

        if (userRepository.existsByRole(Role.ADMIN)) {
            logger.info("Admin bootstrap skipped because an ADMIN user already exists.");
            return;
        }

        if (isBlank(username) || isBlank(email) || isBlank(password)) {
            logger.warn("Admin bootstrap enabled but username/email/password is missing. Skipping admin creation.");
            return;
        }

        try {
            userBusinessRules.checkIfUsernameNotExists(username);
            userBusinessRules.checkIfEmailNotExists(email);
            userBusinessRules.checkIfUsernameIsValid(username);
            userBusinessRules.checkIfEmailIsValid(email);
            userBusinessRules.checkIfPasswordIsValid(password);

            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(isBlank(fullName) ? "System Administrator" : fullName.trim());
            user.setRole(Role.ADMIN);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);
            logger.info("Bootstrap admin user created successfully for username '{}'.", user.getUsername());
        } catch (Exception exception) {
            logger.error("Bootstrap admin user could not be created: {}", exception.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
