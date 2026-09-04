package com.example.demo.dto;

import com.example.demo.entities.users.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDetails implements UserDetails {
    private Long id ;
    private String email ;
    private String password ;

    private Collection<GrantedAuthority> authorities ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public static UtilisateurDetails buildUtilisateurDetails(Utilisateur utilisateur){
        List<GrantedAuthority> authorities = utilisateur.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_ " + role.getRole())
        ).collect(Collectors.toList());
        return new UtilisateurDetails(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getPassword(),
                authorities
        );
    }

}
