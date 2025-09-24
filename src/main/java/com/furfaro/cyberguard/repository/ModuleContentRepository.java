package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.ModuleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuleContentRepository extends JpaRepository<ModuleContent, Long> {
    
    List<ModuleContent> findByModuleIdOrderByOrderIndexAsc(Long moduleId);
}