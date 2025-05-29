package com.api.server.controller.common;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.service.common.FileService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/{category}")
    public ResponseEntity<FileUploadResponse> uploadFile(
        @PathVariable String category,
        @RequestParam("file") MultipartFile file
    ) {
        FileUploadResponse response = fileService.uploadFile(file, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{category}/{fileName}")
    public ResponseEntity<Resource> getFile(
        @PathVariable String category,
        @PathVariable String fileName
    ) {
        Resource resource = fileService.getFile(category, fileName);

        try {
            // Detectar el tipo de contenido del archivo
            Path filePath = Paths.get("uploads", category, fileName);
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (IOException e) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
    }

    @DeleteMapping("/{category}/{fileName}")
    public ResponseEntity<Void> deleteFile(
        @PathVariable String category,
        @PathVariable String fileName
    ) {
        fileService.deleteFile(fileName, category);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{category}/{fileName}/exists")
    public ResponseEntity<Boolean> checkFileExists(
        @PathVariable String category,
        @PathVariable String fileName
    ) {
        boolean exists = fileService.fileExists(fileName, category);
        return ResponseEntity.ok(exists);
    }
}
