package com.api.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.dto.timeline.CreateTimelineRequest;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.timeline.Progress;
import com.api.server.persistence.entity.timeline.ProgressDetail;
import com.api.server.persistence.repository.order.OrderDetailRepository;
import com.api.server.persistence.repository.security.UserRepository;
import com.api.server.persistence.repository.timeline.ProgressDetailRepository;
import com.api.server.persistence.repository.timeline.ProgressRepository;
import com.api.server.service.common.FileService;
import com.api.server.service.timeline.ProgressService;

@Service
public class ProgressServiceImpl implements ProgressService{
    
    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private ProgressDetailRepository progressDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Override
    public Optional<Progress> findProgressByOrderDetailId(Long orderDetailId) {
        Progress progress = progressRepository.findProgressByOrderDetailId(orderDetailId)
            .orElseThrow(() -> new ObjectNotFoundException("Línea de tiempo no encontrada."));
        return Optional.of(progress);
    }

    @Override
    public Progress registerProgressWithDetail(Long orderDetailId, CreateTimelineRequest createTimelineRequest, List<MultipartFile> imageFiles) {
        Progress progress = new Progress();

        progress.setPercentage(0.25);
        progress.setOrderDetail(orderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada.")));
                
        progressRepository.save(progress);

        ProgressDetail progressDetail = new ProgressDetail();

        // Procesar múltiples imágenes
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "timeline");
                    imagePaths.add(fileUploadResponse.getFileUrl());
                }
            }
            // Guardar las rutas de las imágenes como JSON
            progressDetail.setImagepath(String.join(",", imagePaths));
        }

        progressDetail.setDescription(createTimelineRequest.getDescription());
        progressDetail.setDate(createTimelineRequest.getDate());
        progressDetail.setStage(ProgressDetail.ProgressDetailStage.valueOf(createTimelineRequest.getStage()));
        progressDetail.setUser(userRepository.findById(createTimelineRequest.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado.")));
        progressDetail.setProgress(progress);

        progressDetailRepository.save(progressDetail);
        
        return progress;
    }

}
