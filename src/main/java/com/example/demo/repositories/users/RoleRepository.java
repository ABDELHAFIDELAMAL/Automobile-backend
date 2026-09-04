package com.example.demo.repositories.users;

import com.example.demo.entities.users.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AppRole , Long> {
}
