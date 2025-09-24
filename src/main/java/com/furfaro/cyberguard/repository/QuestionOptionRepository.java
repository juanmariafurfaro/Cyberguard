package com.furfaro.cyberguard.repository;

import com.furfaro.cyberguard.models.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
    List<QuestionOption> findByQuestionIdOrderByOrderIndexAsc(Long questionId);
    
    QuestionOption findByQuestionIdAndIsCorrectTrue(Long questionId);
}