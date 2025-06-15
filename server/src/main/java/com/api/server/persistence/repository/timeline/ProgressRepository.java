package com.api.server.persistence.repository.timeline;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.timeline.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long>{
    
    Optional<Progress> findProgressByOrderDetailId(Long orderDetailId);

}
