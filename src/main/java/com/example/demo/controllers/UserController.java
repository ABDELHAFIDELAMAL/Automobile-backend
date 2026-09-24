package com.example.demo.controllers;


import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponse response = new ApiResponse("Users list fetched successfully", users, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        ApiResponse response = new ApiResponse("User found successfully", user, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/by/username")
    public ResponseEntity<ApiResponse> getUserByUsername(@RequestParam String username) {
        Optional<User> user = userService.getUserByUsername(username);
        ApiResponse response = new ApiResponse("User search completed", user, true);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            ApiResponse response = new ApiResponse("User created successfully", createdUser, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        ApiResponse response = new ApiResponse("User updated successfully", updatedUser, true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse response = new ApiResponse("User deleted successfully", null, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/activate/{id}")
    public ResponseEntity<ApiResponse> activate(@PathVariable Long id) {
        User user = userService.activate(id);
        ApiResponse response = new ApiResponse("User activated successfully", user, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivate(@PathVariable Long id) {
        User user = userService.deactivate(id);
        ApiResponse response = new ApiResponse("User deactivated successfully", user, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change/{id}")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable Long id,
                                                      @RequestParam String oldPassword,
                                                      @RequestParam String newPassword) {
        User user = userService.changePassword(id, oldPassword, newPassword);
        ApiResponse response = new ApiResponse("Password changed successfully", user, true);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/assign/role/{id}")
    public ResponseEntity<ApiResponse> assignRole(@PathVariable Long id, @RequestBody Role role) {
        userService.assignRole(id, role);
        ApiResponse response = new ApiResponse("Role assigned successfully", null, true);
        return ResponseEntity.ok(response);
    }
}
