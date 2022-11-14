package com.ncourses.authuser.user.repository;

import com.ncourses.authuser.user.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserEntity> findByUsername(String username);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserEntity> findById(UUID userId);

}
