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
@Table(name="transaction")
@EntityListeners(AuditingEntityListener.class)
public class Transaction{

    @Id
    @GeneratedValue(generator="txn_seq")
    @SequenceGenerator(name="txn_seq",  sequenceName="txnSeq", initialValue = 10000, allocationSize= 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Account seller;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Account buyer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "listing_id", referencedColumnName = "id")
    private Listing listing;

    private Integer purchaseQuantity;

    private BigDecimal purchaseAmount;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


}
