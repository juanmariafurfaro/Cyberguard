package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.ModuleProgress;
import com.furfaro.cyberguard.models.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {
    
    List<ModuleProgress> findByUserId(Long userId);
    
    Optional<ModuleProgress> findByUserIdAndModuleId(Long userId, Long moduleId);
    
    List<ModuleProgress> findByUserIdAndModule_CourseId(Long userId, Long courseId);
    
    @Query("SELECT mp FROM ModuleProgress mp WHERE mp.user.id = :userId AND mp.module.course.id = :courseId ORDER BY mp.module.orderIndex")
    List<ModuleProgress> findByUserIdAndCourseIdOrderByModuleIndex(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(mp) FROM ModuleProgress mp WHERE mp.user.id = :userId AND mp.module.course.id = :courseId AND mp.status = :status")
    Long countByUserIdAndCourseIdAndStatus(@Param("userId") Long userId, @Param("courseId") Long courseId, @Param("status") ProgressStatus status);
}