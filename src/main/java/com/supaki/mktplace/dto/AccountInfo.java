package com.supaki.mktplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supaki.mktplace.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfo {

     @NotBlank(message = "account name can't be blank!")
     @Size(min=1, max = 20, message = "account name can't have more than 20 characters! ")
     private String  accountName;
     @Min(value = 16, message = "minimum age for creating account is 16 years!")
     private Integer age;

     @Enumerated(EnumType.STRING)
     private Gender gender;

     private String countryCode;

     @DecimalMin(value = "0.0", message = "account balance can't be negative !")
     BigDecimal accountBalance;

  //   List<ItemInfo> items;
}
