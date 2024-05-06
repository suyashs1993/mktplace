package com.supaki.mktplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfo {

 @NotBlank(message = "Item name can't be blank")
 @Size(min=1, max = 20, message = "item name can't have more than 20 characters!")
 private String itemName;

//this is different from selling or listed price
 private BigDecimal itemPrice;
 private String itemDescription;
 @Size(min=1, max = 20, message = " item category can't have more than 20 characters!")
 private String itemCategory;
}
