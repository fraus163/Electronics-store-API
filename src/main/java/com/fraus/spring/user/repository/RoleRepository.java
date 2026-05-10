package com.fraus.spring.user.repository;

import com.fraus.spring.user.repository.entity.Role;
import com.fraus.spring.user.repository.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(UserRole name);
}
