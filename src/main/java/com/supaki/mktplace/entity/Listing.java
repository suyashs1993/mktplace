package com.supaki.mktplace.entity;
import com.supaki.mktplace.constants.Constants;
import com.supaki.mktplace.enums.ListingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name= "listing",uniqueConstraints = {
        @UniqueConstraint(name = "seller_item_status", columnNames = {"seller_id","item_id", "status"})})
public class Listing {

    @Id
    @GeneratedValue(generator="listing_seq")
    @SequenceGenerator(name="listing_seq",  sequenceName="listingSeq", initialValue = 1000, allocationSize= 5)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Account seller;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    private Integer sellingQuantity;

    //this can be different from actual item price
    private BigDecimal sellingPrice;

    @Enumerated(EnumType.STRING)
    private ListingStatus status = ListingStatus.LISTED;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
