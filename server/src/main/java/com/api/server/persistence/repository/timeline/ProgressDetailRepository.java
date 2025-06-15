package com.api.server.persistence.repository.timeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.timeline.ProgressDetail;

public interface ProgressDetailRepository extends JpaRepository<ProgressDetail, Long>{

    Page<ProgressDetail> findByProgressOrderDetailId(Long orderDetailId, Pageable pageable);
    
}
