package com.example.demo.jwt;

import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public String generateToken(@Email String email) {
        return "";
    }
}
