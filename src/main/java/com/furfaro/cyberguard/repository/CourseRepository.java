package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
   
    List<Course> findByIsActiveTrue();
    
    // Método adicional útil
    List<Course> findByIsActiveTrueOrderByCreatedAtAsc();
}