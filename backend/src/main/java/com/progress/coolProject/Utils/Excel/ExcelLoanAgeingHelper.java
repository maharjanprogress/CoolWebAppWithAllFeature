package com.progress.coolProject.Utils.Excel;


import com.progress.coolProject.DTO.Excel.LoanAccountAgeingDTO;
import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.LoanPayerCategory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelLoanAgeingHelper {
    Map<String, LoanAccountAgeingDTO> rowsMap;
    public ExcelLoanAgeingHelper(Map<String, LoanAccountAgeingDTO> rowsMap) { this.rowsMap = rowsMap; }

    /**
     * Filter loans by payment category
     * @param categories One or more LoanPayerCategory to filter by
     * @return Filtered map containing only loans matching the specified categories
     */
    public Map<String, LoanAccountAgeingDTO> getFilteredPayCategory(LoanPayerCategory... categories) {
        if (categories == null || categories.length == 0) {
            return new HashMap<>(rowsMap);
        }

        // Convert varargs to a set for efficient lookup
        var categorySet = Arrays.stream(categories).collect(Collectors.toSet());

        return rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getPaymentType() != null
                        && categorySet.contains(entry.getValue().getPaymentType()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
    public Map<String, LoanAccountAgeingDTO> getSortedLoanCategory() {
        return rowsMap.entrySet().stream()
                // ignore null balances, or include them at the end depending on your preference
                .sorted((e1, e2) -> {
                    Double b1 = e1.getValue().getBalanceAmount();
                    Double b2 = e2.getValue().getBalanceAmount();

                    // Handle nulls safely (null = treated as 0 or pushed to end)
                    if (b1 == null && b2 == null) return 0;
                    if (b1 == null) return 1;   // null goes to bottom
                    if (b2 == null) return -1;

                    // Sort descending
                    return b2.compareTo(b1);
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,  // merge function (won’t be used)
                        LinkedHashMap::new
                ));
    }

    public Map<String, LoanAccountAgeingDTO> getFilteredAccountName(LoanCategory... loanTypes){
        if (loanTypes == null || loanTypes.length == 0) {
            return new HashMap<>(rowsMap);
        }

        var categorySet = Arrays.stream(loanTypes).collect(Collectors.toSet());

        return rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getAccountName() !=null
                        && categorySet.contains(entry.getValue().getAccountName()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public Double getTotalBalanceByCategory(Integer limit, LoanPayerCategory... categories) {
        // Start with all loans
        var loansStream = rowsMap.values().stream()
                .filter(loan -> loan.getBalanceAmount() != null);

        // Apply limit first if specified
        if (limit != null && limit > 0) {
            loansStream = loansStream
                    .sorted((l1, l2) -> l2.getBalanceAmount().compareTo(l1.getBalanceAmount()))
                    .limit(limit);
        }

        // Apply category filter if provided
        if (categories != null && categories.length > 0) {
            var categorySet = Arrays.stream(categories).collect(Collectors.toSet());
            loansStream = loansStream
                    .filter(loan -> loan.getPaymentType() != null && categorySet.contains(loan.getPaymentType()));
        }

        // Sum balances
        return loansStream
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public Double getTotalBalance() {
        return rowsMap.values().stream()
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }


}
