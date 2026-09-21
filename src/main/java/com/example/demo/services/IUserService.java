package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User activate(Long id);
    User deactivate(Long id);
    User changePassword(Long id, String oldPassword, String newPassword);
    void assignRole(Long id, Role role);
}
