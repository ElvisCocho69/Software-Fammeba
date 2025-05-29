package com.api.server.service.impl;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.dto.structure.CreateDesignRequest;
import com.api.server.dto.structure.DesignResponse;
import com.api.server.dto.structure.StructureDTO;
import com.api.server.dto.structure.UpdateDesignRequest;
import com.api.server.persistence.entity.structure.Design;
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.persistence.repository.structure.DesignRepository;
import com.api.server.persistence.repository.structure.StructureRepository;
import com.api.server.service.structure.DesignService;
import com.api.server.service.common.FileService;

@Service
public class DesignServiceImpl implements DesignService {

    @Autowired
    private DesignRepository designRepository;

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private FileService fileService;

    @Override
    public DesignResponse createDesign(CreateDesignRequest request, MultipartFile imageFile) {
        Structure structure = structureRepository.findById(request.getStructureId()).orElseThrow(() -> new RuntimeException("Estructura no encontrada"));

        Design design = new Design();
        design.setName(request.getName());
        design.setDescription(request.getDescription());
        
        FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "designs");
        design.setImagepath(fileUploadResponse.getFileUrl());

        design.setVersion(request.getVersion());
        design.setCreatedAt(Date.valueOf(LocalDateTime.now().toLocalDate()));
        design.setUpdatedAt(Date.valueOf(LocalDateTime.now().toLocalDate()));
        design.setStructure(structure);
        designRepository.save(design);
        return mapToDesignResponse(design);
    }

    @Override
    public DesignResponse updateDesign(Long id, UpdateDesignRequest request, MultipartFile imageFile) {
        Design design = designRepository.findById(id).orElseThrow(() -> new RuntimeException("Diseño no encontrado"));
        boolean hasChanges = false;

        if (request.getName() != null && !request.getName().equals(design.getName())) {
            design.setName(request.getName());
            hasChanges = true;
        }
        if (request.getDescription() != null && !request.getDescription().equals(design.getDescription())) {
            design.setDescription(request.getDescription());
            hasChanges = true;
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "designs");
            design.setImagepath(fileUploadResponse.getFileUrl());
            hasChanges = true;
        }
        
        if (request.getVersion() != null && !request.getVersion().equals(design.getVersion())) {
            design.setVersion(request.getVersion());
            hasChanges = true;
        }
        
        if (hasChanges) {
            design.setUpdatedAt(Date.valueOf(LocalDateTime.now().toLocalDate()));
            designRepository.save(design);
        }
        
        return mapToDesignResponse(design);
    }
    
    @Override
    public DesignResponse getDesignById(Long id) {
        Design design = designRepository.findById(id).orElseThrow(() -> new RuntimeException("Diseño no encontrado"));
        return mapToDesignResponse(design);
    }
    
    @Override
    public DesignResponse getDesignByStructureId(Long structureId) {
        Design design = designRepository.findByStructureId(structureId, Pageable.unpaged()).getContent().get(0);
        return mapToDesignResponse(design);
    }

    @Override
    public Page<DesignResponse> getDesigns(Pageable pageable) {
        Page<Design> designs = designRepository.findAll(pageable);
        return designs.map(this::mapToDesignResponse);
    }

    @Override
    public void deleteDesign(Long id) {
        designRepository.deleteById(id);
    }
    
    private DesignResponse mapToDesignResponse(Design design) {
        DesignResponse designResponse = new DesignResponse();
        designResponse.setId(design.getId());
        designResponse.setName(design.getName());
        designResponse.setDescription(design.getDescription());
        designResponse.setImagepath(design.getImagepath());
        designResponse.setVersion(design.getVersion());
        designResponse.setCreatedAt(design.getCreatedAt());
        designResponse.setUpdatedAt(design.getUpdatedAt());
        designResponse.setStructure(mapToStructureDTO(design.getStructure()));
        return designResponse;
    }

    private StructureDTO mapToStructureDTO(Structure structure) {
    StructureDTO structureDTO = new StructureDTO();
    structureDTO.setId(structure.getId());
    structureDTO.setName(structure.getName());
    structureDTO.setDescription(structure.getDescription());
    return structureDTO;
}
}
