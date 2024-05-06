//package com.supaki.mktplace.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//@MappedSuperclass
//@Getter
//public abstract class BaseEntity implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy= GenerationType.SEQUENCE)
//    private Long id;
//
//
//    @Column(name = "created_date")
//    private LocalDateTime createdDate = LocalDateTime.now();
//
//    @Column(name = "updated_date")
//    private LocalDateTime lastModifiedDate = LocalDateTime.now();
//}
