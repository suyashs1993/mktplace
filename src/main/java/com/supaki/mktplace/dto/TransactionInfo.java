package com.supaki.mktplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInfo {
    @NotBlank(message = "buyer name can't be blank")
    private String  buyerName;
    @NotNull(message = "purchase quantity should be provided!")
    @Min(value = 1, message = "purchase quantity should be positive !")
    private Integer purchaseQty;
    @NotNull(message = "purchase amount should be provided!")
    @DecimalMin(value = "0.0", inclusive = false, message = "purchase amount should be positive !")
    private BigDecimal purchaseAmount;
    @NotNull(message = "listing details should be there for purchasing item !")
    private ListingInfo listing;
}
