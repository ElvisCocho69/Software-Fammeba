package com.api.server.persistence.repository.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialMovement;

@Repository
public interface MaterialMovementRepository extends JpaRepository<MaterialMovement, Long> {
    
    Page<MaterialMovement> findByMaterialId(Long materialId, Pageable pageable);

    Page<MaterialMovement> findByMaterialCode(String materialCode, Pageable pageable);

    Page<MaterialMovement> findByMaterialStatus(Material.MaterialStatus materialStatus, Pageable pageable);

}
