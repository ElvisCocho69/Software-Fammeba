package com.api.server.service.structure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.structure.CreateDesignRequest;
import com.api.server.dto.structure.DesignResponse;
import com.api.server.dto.structure.UpdateDesignRequest;

public interface DesignService {

    DesignResponse createDesign(CreateDesignRequest request, MultipartFile imageFile);

    DesignResponse updateDesign(Long id, UpdateDesignRequest request, MultipartFile imageFile);
    
    DesignResponse getDesignById(Long id);
    
    DesignResponse getDesignByStructureId(Long structureId);
    
    Page<DesignResponse> getDesigns(Pageable pageable);    
    
    void deleteDesign(Long id);
    
}
