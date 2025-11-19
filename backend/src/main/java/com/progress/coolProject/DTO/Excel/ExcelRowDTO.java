package com.progress.coolProject.DTO.Excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRowDTO {
    private Integer ledgerNo;
    private String ledgerDescription;
    private Double debit;
    private Double credit;
    private int rowNumber; // For error reporting

    public boolean isValid() {
        return ledgerNo != null && ledgerDescription != null && !ledgerDescription.trim().isEmpty();
    }

    public String getValidationError() {
        if (ledgerNo == null) {
            return "Ledger No is missing";
        }
        if (ledgerDescription == null || ledgerDescription.trim().isEmpty()) {
            return "Ledger Description is missing";
        }
        return null;
    }
}