package com.example.demo.auth;

import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String , String> register(@RequestBody RegisterRequest request) throws EmailAlreadyExistsException {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Map<String , String> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

}
