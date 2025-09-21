package com.furfaro.cyberguard.repository;


import com.furfaro.cyberguard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = ?1 WHERE u.email = ?2")
    void updateFailAttempts(int failAttempts, String email);
    
    @Modifying
    @Query("UPDATE User u SET u.accountLockedTime = ?1 WHERE u.email = ?2")
    void lockAccount(LocalDateTime lockTime, String email);
    
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = 0, u.accountLockedTime = null WHERE u.email = ?1")
    void unlockAccount(String email);
}
