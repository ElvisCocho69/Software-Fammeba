package com.api.server.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialCategory;
import com.api.server.persistence.entity.material.MaterialInventory;
import com.api.server.persistence.entity.material.MaterialMovement;
import com.api.server.persistence.repository.material.MaterialCategoryRepository;
import com.api.server.persistence.repository.material.MaterialInventoryRepository;
import com.api.server.persistence.repository.material.MaterialMovementRepository;
import com.api.server.persistence.repository.material.MaterialRepository;
import com.api.server.service.material.MaterialService;

import jakarta.transaction.Transactional;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMovementRepository movementRepository;

    @Autowired
    private MaterialInventoryRepository inventoryRepository;

    @Autowired
    private MaterialCategoryRepository materialCategoryRepository;

    // Método para obtener todos los materiales, con filtros por categoria y estado
    @Override
    public Page<Material> findAll(String materialCategoryName, String status, Pageable pageable) {
        Page<Material> materials;
        if (materialCategoryName != null && status != null) {
            materials = materialRepository.findByMaterialcategoryNameAndStatus(materialCategoryName, Material.MaterialStatus.valueOf(status), pageable);
        } else if (materialCategoryName != null) {
            materials = materialRepository.findByMaterialcategoryName(materialCategoryName, pageable);
        } else if (status != null) {
            materials = materialRepository.findByStatus(Material.MaterialStatus.valueOf(status), pageable);
        } else {
            materials = materialRepository.findAll(pageable);
        }
        return materials;
    }

    // Método para guardar un nuevo material
    @Override
    public Material saveMaterial(Material material) {
        Material newMaterial = new Material();

        newMaterial.setName(material.getName());
        newMaterial.setDescription(material.getDescription());
        newMaterial.setCode(material.getName().toUpperCase().substring(0, 3) + String.format("%04d", materialRepository.count() + 1));
        newMaterial.setMeasurementunit(material.getMeasurementunit());
        newMaterial.setMaterialcategory(materialCategoryRepository.findById(material.getMaterialcategory().getId()).orElseThrow(() -> new IllegalArgumentException("Categoria de material no encontrada")));
        newMaterial.setStatus(material.getStatus());
        materialRepository.save(newMaterial);

        inventoryRepository.findByMaterialId(newMaterial.getId()).orElseGet(() -> {
            MaterialInventory inventory = new MaterialInventory();
            inventory.setMaterial(newMaterial);
            inventory.setQuantity(0.0);
            inventoryRepository.save(inventory);
            return inventory;
        });

        return newMaterial;
    }

    // Método para actualizar un material
    @Override
    public Material updateMaterial(Long id, Material material) {
        Material existingMaterial = materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));

        if (material.getName() != null) {
            existingMaterial.setName(material.getName());
        }

        if (material.getDescription() != null) {
            existingMaterial.setDescription(material.getDescription());
        }

        if (material.getMeasurementunit() != null) {
            existingMaterial.setMeasurementunit(material.getMeasurementunit());
        }

        if (material.getMaterialcategory() != null) {
            existingMaterial.setMaterialcategory(materialCategoryRepository.findById(material.getMaterialcategory().getId()).orElseThrow(() -> new IllegalArgumentException("Categoria de material no encontrada")));
        }

        if (material.getStatus() != null) {
            existingMaterial.setStatus(material.getStatus());
        }

        return materialRepository.save(existingMaterial);
    }

    // Método para obtener todas las categorías de materiales, con filtros por estado
    @Override
    public Page<MaterialCategory> findAll(String status, Pageable pageable) {
        Page<MaterialCategory> materialCategories;
        if (status != null) {
            materialCategories = materialCategoryRepository.findByStatus(MaterialCategory.CategoryStatus.valueOf(status), pageable);
        } else {
            materialCategories = materialCategoryRepository.findAll(pageable);
        }
        return materialCategories;
    }

    // Método para guardar una nueva categoría de material
    @Override
    public MaterialCategory saveMaterialCategory(MaterialCategory materialCategory) {
        MaterialCategory newMaterialCategory = new MaterialCategory();
        newMaterialCategory.setName(materialCategory.getName());
        newMaterialCategory.setStatus(materialCategory.getStatus());
        return materialCategoryRepository.save(newMaterialCategory);
    }

    // Método para actualizar una categoría de material
    @Override
    public MaterialCategory updateMaterialCategory(Long id, MaterialCategory materialCategory) {
        MaterialCategory existingMaterialCategory = materialCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria de material no encontrada"));

        if (materialCategory.getName() != null) {
            existingMaterialCategory.setName(materialCategory.getName());
        }

        if (materialCategory.getStatus() != null) {
            existingMaterialCategory.setStatus(materialCategory.getStatus());
        }

        return materialCategoryRepository.save(existingMaterialCategory);
    }

    // Método para obtener una categoría de material por su ID
    @Override
    public Optional<MaterialCategory> getMaterialCategoryById(Long id) {
        MaterialCategory existingMaterialCategory = materialCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria de material no encontrada"));
        return Optional.of(existingMaterialCategory);
    }

    // Método para obtener un material por su código
    @Override
    public Optional<Material> getMaterialByCode(String code) {
        Material existingMaterial = materialRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));
        return Optional.of(existingMaterial);
    }

    // Método para obtener un material por su ID
    @Override
    public Optional<Material> getMaterialById(Long id) {
        Material existingMaterial = materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));
        return Optional.of(existingMaterial);
    }

    // Método para obtener los movimientos de un material por su código
    @Override
    public Page<MaterialMovement> getMaterialMovements(String materialCode, Pageable pageable) {
        return movementRepository.findByMaterialCode(materialCode, pageable);
    }

    @Override
    public Page<MaterialMovement> getAllMovements(Pageable pageable) {
        return movementRepository.findAll(pageable);
    }

    // Método para registrar un movimiento de material
    @Transactional
    public MaterialMovement registerMovement(String materialCode, String movementType, Double quantity, String description, Long userId) {
        Material material = materialRepository.findByCode(materialCode).orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));
        
        MaterialInventory inventory = inventoryRepository.findByMaterialId(material.getId()).orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));

        Double currentStock = inventory.getQuantity();
        Double updatedStock = currentStock;

        switch (MaterialMovement.MovementType.valueOf(movementType)) {
            case IN:
            case RETURN:
                updatedStock += quantity;
                break;

            case OUT:
            case LOSS:
                if (quantity > currentStock) {
                    throw new IllegalArgumentException("No hay suficiente stock disponible");
                }
                updatedStock -= quantity;
                break;

            case ADJUSTMENT:
                updatedStock += quantity;
                break;
        }

        if (updatedStock < 0 && !MaterialMovement.MovementType.valueOf(movementType).equals(MaterialMovement.MovementType.ADJUSTMENT)) {
            throw new IllegalArgumentException("No se puede realizar el movimiento. El stock no puede ser negativo");
        }

        inventory.setQuantity(updatedStock);
        inventoryRepository.save(inventory);

        MaterialMovement movement = new MaterialMovement();
        movement.setMaterial(material);
        movement.setMovementtype(MaterialMovement.MovementType.valueOf(movementType));
        movement.setQuantity(quantity);
        movement.setDescription(description);
        movement.setUserid(userId);
        movement.setMovementdate(LocalDateTime.now());
        
        return movementRepository.save(movement);
    }

    // Método para obtener el inventario de un material por su código
    @Override
    public Optional<MaterialInventory> getInventoryByMaterialCode(String materialCode) {
        MaterialInventory existingMaterialInventory = inventoryRepository.findByMaterialCode(materialCode).orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));
        return Optional.of(existingMaterialInventory);
    }
}
