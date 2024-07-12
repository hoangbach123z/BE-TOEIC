package com.backend.spring.repository;

import com.backend.spring.entity.ERole;
import com.backend.spring.entity.Role;
import com.backend.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole role);
}
