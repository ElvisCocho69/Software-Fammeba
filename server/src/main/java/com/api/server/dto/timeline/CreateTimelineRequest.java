package com.api.server.dto.timeline;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateTimelineRequest {

    private String imagepath;

    private String description;

    private LocalDateTime date;

    private String stage;

    private Long userId;

}
