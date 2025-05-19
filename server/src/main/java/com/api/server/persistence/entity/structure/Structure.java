// package com.api.server.persistence.entity.structure;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Table(name = "Structures")
// @Getter
// @Setter
// public class Structure {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;

//     private String description;

//     @OneToOne(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//     private OrderDetail orderdetail;

//     private String height;

//     private String width;

//     private String depth;

//     private String color;

//     private String material;

//     @Enumerated(EnumType.STRING)
//     private String status;

//     public static enum Status {
//         PENDIENTE,
//         COMPLETADO,
//         CANCELADO
//     }

//     private String estimatedweight;

//     private Date startdate;

//     private Date enddateestimated;

//     private Date enddatereal;

//     private String observations;

//    

// }