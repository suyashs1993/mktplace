package com.supaki.mktplace.entity;

import com.supaki.mktplace.enums.AccountType;
import com.supaki.mktplace.enums.Gender;
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
@Table(name="account")
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(generator="account_seq")
    @SequenceGenerator(name="account_seq",  sequenceName="accountSeq", initialValue = 1, allocationSize=1)
    private Long id;

    @Column(unique = true, length = 20)
    private String  accountName;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private AccountType accountType = AccountType.USER ;

    @Column(name="account_balance")
    private BigDecimal accountBalance = new BigDecimal(0);

    private String countryCode;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
