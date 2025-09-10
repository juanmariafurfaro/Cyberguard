package com.furfaro.cyberguard.repository.content;

import com.furfaro.cyberguard.models.content.Resource;
import com.furfaro.cyberguard.models.content.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByTipo(ResourceType tipo);
}