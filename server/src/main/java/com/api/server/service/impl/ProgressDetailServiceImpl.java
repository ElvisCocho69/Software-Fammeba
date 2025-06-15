package com.api.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.dto.timeline.CreateProgressDetailRequest;
import com.api.server.dto.timeline.UpdateProgressDetailRequest;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.timeline.ProgressDetail;
import com.api.server.persistence.repository.security.UserRepository;
import com.api.server.persistence.repository.timeline.ProgressDetailRepository;
import com.api.server.persistence.repository.timeline.ProgressRepository;
import com.api.server.service.common.FileService;
import com.api.server.service.timeline.ProgressDetailService;

@Service
public class ProgressDetailServiceImpl implements ProgressDetailService{

    @Autowired
    private ProgressDetailRepository progressDetailRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Override
    public Page<ProgressDetail> findAllByOrderDetailId(Long orderDetailId, Pageable pageable) {
        Page<ProgressDetail> progresses = progressDetailRepository.findByProgressOrderDetailId(orderDetailId, pageable);
        if (progresses.isEmpty()) {
            throw new ObjectNotFoundException("Línea de tiempo no encontrada.");
        }
        return progresses;
    }

    @Override
    public ProgressDetail registerProgressDetail(Long progressId, CreateProgressDetailRequest createProgressDetailRequest, List<MultipartFile> imageFiles) {
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
         
         progressDetail.setDescription(createProgressDetailRequest.getDescription());
         progressDetail.setDate(createProgressDetailRequest.getDate());
         progressDetail.setStage(createProgressDetailRequest.getStage());
         progressDetail.setUser(userRepository.findById(createProgressDetailRequest.getUserId())
             .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado.")));
         progressDetail.setProgress(progressRepository.findById(progressId)
             .orElseThrow(() -> new ObjectNotFoundException("Progreso no encontrado.")));

         return progressDetail;
    }

    @Override
    public ProgressDetail findProgressDetailById(Long progressDetailId) {
        return progressDetailRepository.findById(progressDetailId)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la línea de tiempo no encontrado."));
    }

    @Override
    public ProgressDetail updateProgressDetail(Long progressId, UpdateProgressDetailRequest updateProgressDetailRequest, List<MultipartFile> imageFiles, String existingImagePaths) {
        ProgressDetail progressDetail = progressDetailRepository.findById(progressId)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de progreso no encontrado."));

        // Procesar imágenes existentes y nuevas
        List<String> allImagePaths = new ArrayList<>();
        
        // Agregar las imágenes existentes que se mantienen
        if (existingImagePaths != null && !existingImagePaths.isEmpty()) {
            allImagePaths.addAll(Arrays.asList(existingImagePaths.split(",")));
        }
        
        // Procesar las nuevas imágenes
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "timeline");
                    allImagePaths.add(fileUploadResponse.getFileUrl());
                }
            }
        }
        
        // Actualizar el imagepath con todas las rutas
        if (!allImagePaths.isEmpty()) {
            progressDetail.setImagepath(String.join(",", allImagePaths));
        }

        if (updateProgressDetailRequest.getDescription() != null) {
            progressDetail.setDescription(updateProgressDetailRequest.getDescription());
        }

        if (updateProgressDetailRequest.getDate() != null) {
            progressDetail.setDate(updateProgressDetailRequest.getDate());
        }

        if (updateProgressDetailRequest.getStage() != null) {
            progressDetail.setStage(updateProgressDetailRequest.getStage());
        }

        progressDetailRepository.save(progressDetail);

        return progressDetail;
    }
}
