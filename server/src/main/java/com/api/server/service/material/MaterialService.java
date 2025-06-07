package com.api.server.service.material;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialCategory;
import com.api.server.persistence.entity.material.MaterialInventory;
import com.api.server.persistence.entity.material.MaterialMovement;

public interface MaterialService {

    Page<Material> findAll(String materialCategoryName, String status, Pageable pageable);

    Material saveMaterial(Material material);

    Material updateMaterial(Long id, Material material);

    Optional<Material> getMaterialByCode(String code);

    Optional<Material> getMaterialById(Long id);

    Page<MaterialMovement> getMaterialMovements(String materialCode, Pageable pageable);

    MaterialMovement registerMovement(String materialCode, String movementType, Double quantity, String description, Long userId);

    Optional<MaterialInventory> getInventoryByMaterialCode(String materialCode);

    Page<MaterialCategory> findAll(String status, Pageable pageable);
    
    MaterialCategory saveMaterialCategory(MaterialCategory materialCategory);

    MaterialCategory updateMaterialCategory(Long id, MaterialCategory materialCategory);

    Optional<MaterialCategory> getMaterialCategoryById(Long id);

}
