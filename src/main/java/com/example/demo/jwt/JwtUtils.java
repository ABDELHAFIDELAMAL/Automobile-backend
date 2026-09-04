package com.example.demo.jwt;

import com.example.demo.dto.UtilisateurDetails;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret ;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime ;

    public String generateToken(Authentication authentication) {
        UtilisateurDetails utilisateurPrincipal = (UtilisateurDetails) authentication.getPrincipal();
        List<String> roles = utilisateurPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(utilisateurPrincipal.getEmail())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key() , SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // 08 : 30 : 30
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw  new JwtException(e.getMessage());
        }
    }


}
