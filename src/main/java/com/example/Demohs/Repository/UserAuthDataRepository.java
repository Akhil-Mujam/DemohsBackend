package com.example.Demohs.Repository;

import com.example.Demohs.Entity.UserAuthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthDataRepository extends JpaRepository<UserAuthData, UUID> {
    Optional<UserAuthData> findByUsername(String username);

    @Query("SELECT MAX(u.userID) FROM UserAuthData u")
    Integer findMaxUserId();
}
