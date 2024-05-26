package com.warehousemanagement.repository;

import com.warehousemanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u " +
            "FROM UserEntity u " +
            "WHERE u.username = ?1")
    UserEntity findByUsername(String username);
}
