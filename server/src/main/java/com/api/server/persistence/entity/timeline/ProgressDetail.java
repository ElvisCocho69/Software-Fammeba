package com.api.server.persistence.entity.timeline;

import java.time.LocalDateTime;

import com.api.server.persistence.entity.security.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="ProgressDetails")
@Data
@ToString(exclude = "progress")
public class ProgressDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagepath;

    private String description;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private ProgressDetailStage stage;

    public static enum ProgressDetailStage{
        PENDING,
        SELECTED_MATERIALS,
        CUTTING,
        ASSEMBLING,
        FINISHED
    }

    @OneToMany
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "progress_id")
    @JsonBackReference
    private Progress progress;

}
