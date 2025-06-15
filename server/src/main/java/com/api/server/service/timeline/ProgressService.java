package com.api.server.service.timeline;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.timeline.CreateTimelineRequest;
import com.api.server.persistence.entity.timeline.Progress;

public interface ProgressService {
    
    Optional<Progress> findProgressByOrderDetailId(Long orderDetailId);

    Progress registerProgressWithDetail(Long orderDetailId, CreateTimelineRequest createTimelineRequest, List<MultipartFile> imageFiles);

}
