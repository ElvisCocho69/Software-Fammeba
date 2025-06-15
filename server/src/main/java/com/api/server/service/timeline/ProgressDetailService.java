package com.api.server.service.timeline;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.timeline.CreateProgressDetailRequest;
import com.api.server.dto.timeline.UpdateProgressDetailRequest;
import com.api.server.persistence.entity.timeline.ProgressDetail;

public interface ProgressDetailService {

    Page<ProgressDetail> findAllByOrderDetailId(Long orderDetailId, Pageable pageable);

    ProgressDetail registerProgressDetail(Long progressId, CreateProgressDetailRequest CreateProgressDetailRequest, List<MultipartFile> imageFiles);

    ProgressDetail findProgressDetailById(Long progressDetailId);

    ProgressDetail updateProgressDetail(Long progressId, UpdateProgressDetailRequest updateProgressDetailRequest, List<MultipartFile> imageFiles, String existingImagePaths);
}
