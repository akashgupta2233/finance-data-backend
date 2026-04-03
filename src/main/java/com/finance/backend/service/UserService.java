package com.finance.backend.service;

import com.finance.backend.entity.Role;
import com.finance.backend.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserByUsername(String username);

    List<User> listUsers();

    User activateUser(Long id);

    User deactivateUser(Long id);

    User updateUserRoles(Long id, Set<Role> roles);

    User getUserById(Long id);
}
