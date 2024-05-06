package com.supaki.mktplace.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingInfo {
    @NotBlank(message = "seller name can't be blank!")
    private String sellerName;
    @NotBlank(message = "item name can't be blank!")
    private String itemName;
    @Min(value =1, message = "selling quantity should be positive!")
    private Integer sellingQty;
    @DecimalMin(value = "10.0", message = "selling price cannot be less than 10 USD!")
    @DecimalMax(value = "1000.0", message = "selling price cannot be greater than 1000 USD!")
    private BigDecimal sellingPrice;
    private Long itemId;
    private Long listingId;
    private Long sellerId;
}
