// package com.api.server.persistence.entity.structureprogress;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Table(name="Progress")
// @Getter
// @Setter
// public class StructureProgress {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Enumerated
//     private ProgressStage stage;

//     public static enum ProgressStage {
//         APPROVED_DESIGN,
//         SELECTED_MATERIALS,
//         PRODUCTION_STARTED,
//         QUALITY_CHECK,
//         COMPLETED,
//         INCIDENT
//     }

    
// }
