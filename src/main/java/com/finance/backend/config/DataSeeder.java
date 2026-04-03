package com.finance.backend.config;

import com.finance.backend.entity.Role;
import com.finance.backend.entity.User;
import com.finance.backend.entity.UserStatus;
import com.finance.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() != 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("AdminPassword123"));
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(Role.ADMIN));

        userRepository.save(admin);
        System.out.println("Admin user created: admin / AdminPassword123");
    }
}
