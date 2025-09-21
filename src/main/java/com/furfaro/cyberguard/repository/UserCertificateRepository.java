package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Long> {
    
    List<UserCertificate> findByUserId(Long userId);
}