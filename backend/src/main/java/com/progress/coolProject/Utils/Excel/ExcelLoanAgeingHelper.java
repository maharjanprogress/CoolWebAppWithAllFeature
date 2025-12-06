package com.progress.coolProject.Utils.Excel;


import com.progress.coolProject.DTO.Excel.LoanAccountAgeingDTO;
import com.progress.coolProject.Enums.LoanPayerCategory;

import java.util.Arrays;
import java.util.HashMap;
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
}
