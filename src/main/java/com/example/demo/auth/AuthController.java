package com.example.demo.auth;

import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            Object data = authService.register(request) ;
            return ResponseEntity.ok(new ApiResponse(
                    "Utilisateur enregistré avec succès !",
                     data ,
                    true
            ));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email Already Exist" , null , false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Une erreur interne est survenue." , null , false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            Object data = authService.login(request);
            return ResponseEntity.ok(new ApiResponse("Login success", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Login failed", null, false));
        }
    }


    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        try{
            return (Authentication) ResponseEntity.ok(new ApiResponse("Authentication success", authentication, true));
        }catch (AuthenticationException e) {
            return (Authentication) ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid email or password", null, false));
        }
    }

}
