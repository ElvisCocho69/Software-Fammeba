package com.api.server.controller.material;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.MaterialMovementDTO;
import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialCategory;
import com.api.server.persistence.entity.material.MaterialInventory;
import com.api.server.persistence.entity.material.MaterialMovement;
import com.api.server.service.material.MaterialService;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<Page<Material>> findAllMaterials(
        @RequestParam(required = false) String materialCategory,
        @RequestParam(required = false) String status,
        Pageable pageable
    ) {
        try {
            Page<Material> materials = materialService.findAll(materialCategory, status, pageable);
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PostMapping
    public ResponseEntity<Material> saveMaterial(@RequestBody Material material) {
        Material savedMaterial = materialService.saveMaterial(material);
        return ResponseEntity.ok(savedMaterial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        Material updatedMaterial = materialService.updateMaterial(id, material);
        return ResponseEntity.ok(updatedMaterial);
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<Material> getMaterialByCode(@PathVariable String code) {
        Optional<Material> material = materialService.getMaterialByCode(code);
        if (material.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    return ResponseEntity.ok(material.get());
}

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Optional<Material> material = materialService.getMaterialById(id);
        if (material.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(material.get());
    }

    @GetMapping("/movements")
    public ResponseEntity<Page<MaterialMovement>> getAllMovements(Pageable pageable) {
        Page<MaterialMovement> movements = materialService.getAllMovements(pageable);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/movements/{materialCode}")
    public ResponseEntity<Page<MaterialMovement>> getMaterialMovementsFromOneMaterial(
        @PathVariable String materialCode,
        Pageable pageable
    ) {
        Page<MaterialMovement> movements = materialService.getMaterialMovements(materialCode, pageable);
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/movements/{materialCode}")
    public ResponseEntity<MaterialMovement> registerMovement(
        @PathVariable String materialCode,
        @RequestBody MaterialMovementDTO movementDTO
    ) {
        MaterialMovement savedMovement = materialService.registerMovement(
            materialCode, 
            movementDTO.getMovementType(),
            movementDTO.getQuantity(),
            movementDTO.getDescription(),
            movementDTO.getUserId()
        );
        return ResponseEntity.ok(savedMovement);
    }

    @GetMapping("/inventory/{materialCode}")
    public ResponseEntity<Optional<MaterialInventory>> getInventoryByMaterialCode(@PathVariable String materialCode) {
        Optional<MaterialInventory> inventory = materialService.getInventoryByMaterialCode(materialCode);
        if (inventory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/category")
    public ResponseEntity<Page<MaterialCategory>> findAllCategories(
        @RequestParam(required = false) String status,
        Pageable pageable
    ) {
        Page<MaterialCategory> categories = materialService.findAll(status, pageable);
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/category")
    public ResponseEntity<MaterialCategory> saveMaterialCategory(@RequestBody MaterialCategory materialCategory) {
        MaterialCategory savedCategory = materialService.saveMaterialCategory(materialCategory);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<MaterialCategory> updateMaterialCategory(@PathVariable Long id, @RequestBody MaterialCategory materialCategory) {
        MaterialCategory updatedCategory = materialService.updateMaterialCategory(id, materialCategory);
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<MaterialCategory> getMaterialCategoryById(@PathVariable Long id) {
        Optional<MaterialCategory> category = materialService.getMaterialCategoryById(id);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category.get());
    }

    @PostMapping("/category/{id}/disable")
    public ResponseEntity<MaterialCategory> disableMaterialCategory(@PathVariable Long id) {
        MaterialCategory category = new MaterialCategory();
        category.setStatus(MaterialCategory.CategoryStatus.INACTIVE);
        MaterialCategory updatedCategory = materialService.updateMaterialCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

}
