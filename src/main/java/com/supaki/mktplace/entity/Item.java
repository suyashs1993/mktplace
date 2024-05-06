package com.supaki.mktplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@Table(name="item")
@EntityListeners(AuditingEntityListener.class)
public class Item  {
    @Id
    @GeneratedValue(generator="item_seq")
    @SequenceGenerator(name="item_seq",  sequenceName="itemSeq", initialValue = 100, allocationSize= 5)
    private Long id;

    @Column(unique = true, length = 20)
    private String itemName;

    private String itemDescription;

    @Column(length = 20)
    private String itemCategory;

    private BigDecimal itemPrice;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


}
