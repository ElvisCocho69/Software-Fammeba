package com.api.server.dto.timeline;

import java.time.LocalDateTime;

import com.api.server.persistence.entity.timeline.ProgressDetail.ProgressDetailStage;

import lombok.Data;

@Data
public class UpdateProgressDetailRequest {

    private String imagepath;

    private String description;

    private LocalDateTime date;

    private ProgressDetailStage stage;

}
