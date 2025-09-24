package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.Question;
import com.furfaro.cyberguard.models.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByModuleIdOrderByOrderIndexAsc(Long moduleId);
    
    List<Question> findByModuleIdAndQuestionType(Long moduleId, QuestionType questionType);
}