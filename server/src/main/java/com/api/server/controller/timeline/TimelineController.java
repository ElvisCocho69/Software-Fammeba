package com.api.server.controller.timeline;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.timeline.CreateProgressDetailRequest;
import com.api.server.dto.timeline.CreateTimelineRequest;
import com.api.server.dto.timeline.UpdateProgressDetailRequest;
import com.api.server.persistence.entity.timeline.Progress;
import com.api.server.persistence.entity.timeline.ProgressDetail;
import com.api.server.service.timeline.ProgressDetailService;
import com.api.server.service.timeline.ProgressService;

@RestController
@RequestMapping("/timeline")
public class TimelineController {
    @Autowired
    private ProgressService progressService;

    @Autowired
    private ProgressDetailService progressDetailService;

    @GetMapping("/milestones/{orderDetailId}")
    public ResponseEntity<?> listProgressDetailsByOrderDetailId(
        @PathVariable Long orderDetailId,
        Pageable pageable
    ) {
        try {
            Page<ProgressDetail> details = progressDetailService.findAllByOrderDetailId(orderDetailId, pageable);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al obtener los hitos: " + e.getMessage());
        }
    }

    @GetMapping("/milestone/{progressDetailId}")
    public ResponseEntity<?> findProgressDetailById(@PathVariable Long progressDetailId) {
        try {
            ProgressDetail detail = progressDetailService.findProgressDetailById(progressDetailId);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al obtener el hito: " + e.getMessage());
        }
    }

    @PostMapping("/milestones/{progressId}/save")
    public ResponseEntity<?> registerProgressDetail(
        @RequestPart("request") CreateProgressDetailRequest createProgressDetailRequest,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
        @PathVariable Long progressId
    ) {
        try {
            ProgressDetail progressDetail = progressDetailService.registerProgressDetail(progressId, createProgressDetailRequest, imageFiles);
            return ResponseEntity.ok(progressDetail);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al guardar el hito: " + e.getMessage());
        }
    }

    @PutMapping("/milestone/{progressDetailId}/update")
    public ResponseEntity<?> updateProgressDetail(
        @RequestPart("request") UpdateProgressDetailRequest updateProgressDetailRequest,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
        @RequestPart(value = "existingImagePaths", required = false) String existingImagePaths,
        @PathVariable Long progressDetailId
    ) {
        try {
            ProgressDetail progressDetail = progressDetailService.updateProgressDetail(progressDetailId, updateProgressDetailRequest, imageFiles, existingImagePaths);
            return ResponseEntity.ok(progressDetail);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al editar el hito: " + e.getMessage());
        }
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<?> findProgressByOrderDetailId(@PathVariable Long orderDetailId) {
        try {
            Optional<Progress> progress = progressService.findProgressByOrderDetailId(orderDetailId);
            return progress.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al obtener la línea de tiempo: " + e.getMessage());
        }
    }

    @PostMapping("/{orderDetailId}")
    public ResponseEntity<?> registerProgressWithDetail(
        @PathVariable Long orderDetailId,
        @RequestPart(value = "request") CreateTimelineRequest createTimelineRequest,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) {
        try {
            Progress progress = progressService.registerProgressWithDetail(orderDetailId, createTimelineRequest, imageFiles);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error al registrar la línea de tiempo: " + e.getMessage());
        }
    }

}
