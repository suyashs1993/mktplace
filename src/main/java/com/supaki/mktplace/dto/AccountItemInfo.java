package com.supaki.mktplace.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountItemInfo {
    @NotBlank(message = "Item name can't be blank")
    @Size(min=1, max = 20, message = "item name can't have more than 20 characters!")
    private String accountName;
    @Valid
    @NotNull(message = "item list should be present !")
    List<AccountItem> items;
    @Getter
    @AllArgsConstructor
    public static class AccountItem{
        @NotBlank(message = "Item name can't be blank")
        @Size(min=1, max = 20, message = "item name can't have more than 20 characters!")
        String itemName;
        @NotNull(message = "item quantity should be provided!")
        @Min(value =1, message = "item quantity should be positive!")
        Integer quantity;
    }
}
