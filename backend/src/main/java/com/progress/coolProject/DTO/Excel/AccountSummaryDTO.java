package com.progress.coolProject.DTO.Excel;

import com.progress.coolProject.Enums.AccountType;
import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.SavingCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryDTO {
    private Integer code;
    private String description;
    private Double openingAmount;
    private AccountType openingType; // DR or CR
    private Double debit;
    private Double credit;
    private Double balance;
    private AccountType balanceType; // DR or CR

    // Optional: Store the category enum
    private LoanCategory loanCategory;
    private SavingCategory savingCategory;

    // Validation methods
    public boolean isValid() {
        return code != null && code > 0
                && description != null && !description.trim().isEmpty()
                && openingAmount != null
                && openingType != null
                && balance != null
                && balanceType != null;
    }

    public String getValidationError() {
        if (code == null || code <= 0) {
            return "Valid code is required";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is required";
        }
        if (openingAmount == null) {
            return "Opening amount is required";
        }
        if (openingType == null) {
            return "Opening type (DR/CR) is required";
        }
        if (balance == null) {
            return "Balance is required";
        }
        if (balanceType == null) {
            return "Balance type (DR/CR) is required";
        }
        return null;
    }

    // Helper method to check if this is a loan account
    public boolean isLoanAccount() {
        return code != null && code >= 301 && code <= 308;
    }

    // Helper method to check if this is a saving account
    public boolean isSavingAccount() {
        return code != null && code >= 201 && code <= 209;
    }
}
