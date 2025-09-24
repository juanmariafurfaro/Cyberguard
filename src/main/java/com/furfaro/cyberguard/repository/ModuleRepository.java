package com.furfaro.cyberguard.repository;


import com.furfaro.cyberguard.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findByCourseIdOrderByOrderIndexAsc(Long courseId);
    
    Module findByCourseIdAndOrderIndex(Long courseId, Integer orderIndex);
    
    Long countByCourseId(Long courseId);
}