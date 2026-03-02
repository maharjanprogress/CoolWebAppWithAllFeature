package com.progress.coolProject.DTO.Excel;

import com.progress.coolProject.Enums.AccountType;
import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.SavingCategory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberAccountDTO {
    private String accountNumber;
    private String description;
    private Double opening;
    private AccountType openingType; // DR or CR
    private Double debit;
    private Double credit;
    private Double balance;
    private AccountType balanceType; // DR or CR

    private SavingCategory savingCategory;  //if it is a saving account then this will be set
    private LoanCategory loanCategory;  //if it is a loan account then this will be set
}
