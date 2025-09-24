package com.furfaro.cyberguard.repository;


import com.furfaro.cyberguard.models.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    
    List<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId);
    
    Optional<UserAnswer> findByUserIdAndQuestionIdAndAttemptNumber(Long userId, Long questionId, Integer attemptNumber);
    
    @Query("SELECT ua FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.question.module.id = :moduleId")
    List<UserAnswer> findByUserIdAndModuleId(@Param("userId") Long userId, @Param("moduleId") Long moduleId);
    
    @Query("SELECT SUM(ua.pointsEarned) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.question.module.id = :moduleId")
    Optional<Integer> sumPointsEarnedByUserIdAndModuleId(@Param("userId") Long userId, @Param("moduleId") Long moduleId);
}
