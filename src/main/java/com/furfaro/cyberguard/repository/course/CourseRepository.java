package com.furfaro.cyberguard.repository.course;

import com.furfaro.cyberguard.models.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByIsActiveTrue();
    Optional<Course> findByNombreIgnoreCase(String nombre);
}