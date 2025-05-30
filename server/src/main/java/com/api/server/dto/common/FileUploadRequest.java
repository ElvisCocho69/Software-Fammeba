package com.api.server.dto.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadRequest {
    private MultipartFile file;
    private String category;
    private String description;
}
