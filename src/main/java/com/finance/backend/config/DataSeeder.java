package com.finance.backend.config;

import com.finance.backend.entity.Role;
import com.finance.backend.entity.User;
import com.finance.backend.entity.UserStatus;
import com.finance.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
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
        // Only seed if database is empty
        if (userRepository.count() != 0) {
            // Validate existing data integrity
            validateDataIntegrity();
            return;
        }

        System.out.println("=== Seeding initial data ===");

        // Create ADMIN user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("AdminPassword123"));
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(Role.ADMIN));
        userRepository.save(admin);
        System.out.println("✓ Admin user created: admin / AdminPassword123");

        // Create ANALYST user
        User analyst = new User();
        analyst.setUsername("analyst");
        analyst.setEmail("analyst@example.com");
        analyst.setPassword(passwordEncoder.encode("AnalystPassword123"));
        analyst.setStatus(UserStatus.ACTIVE);
        analyst.setRoles(Set.of(Role.ANALYST));
        userRepository.save(analyst);
        System.out.println("✓ Analyst user created: analyst / AnalystPassword123");

        // Create VIEWER user
        User viewer = new User();
        viewer.setUsername("viewer");
        viewer.setEmail("viewer@example.com");
        viewer.setPassword(passwordEncoder.encode("ViewerPassword123"));
        viewer.setStatus(UserStatus.ACTIVE);
        viewer.setRoles(Set.of(Role.VIEWER));
        userRepository.save(viewer);
        System.out.println("✓ Viewer user created: viewer / ViewerPassword123");

        System.out.println("=== Data seeding completed ===");
    }

    private void validateDataIntegrity() {
        System.out.println("=== Validating data integrity ===");

        // Check for users without roles
        List<User> usersWithoutRoles = userRepository.findAll().stream()
                .filter(user -> user.getRoles() == null || user.getRoles().isEmpty())
                .toList();

        if (!usersWithoutRoles.isEmpty()) {
            System.err.println("⚠ WARNING: Found " + usersWithoutRoles.size() + " user(s) without roles:");
            usersWithoutRoles.forEach(user ->
                    System.err.println("  - " + user.getUsername() + " (ID: " + user.getId() + ", Status: " + user.getStatus() + ")")
            );
            System.err.println("  Action required: Assign roles to these users via /users/{id}/roles endpoint");
        }

        // Check role distribution
        long totalUsers = userRepository.count();
        long adminCount = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.ADMIN)).count();
        long analystCount = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.ANALYST)).count();
        long viewerCount = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.VIEWER)).count();

        System.out.println("Role distribution:");
        System.out.println("  - Total users: " + totalUsers);
        System.out.println("  - ADMIN: " + adminCount);
        System.out.println("  - ANALYST: " + analystCount);
        System.out.println("  - VIEWER: " + viewerCount);

        if (analystCount == 0 || viewerCount == 0) {
            System.err.println("⚠ WARNING: Some roles have no users assigned");
        }

        System.out.println("=== Validation completed ===");
    }
}
