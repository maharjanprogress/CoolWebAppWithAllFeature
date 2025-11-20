package com.progress.coolProject.Utils.Excel;

import com.progress.coolProject.DTO.Excel.ExcelRowDTO;
import com.progress.coolProject.Enums.TrialBalanceEnum;

import java.util.Map;

public class ExcelTrialBalanceExcelRowHelper {
    Map<TrialBalanceEnum, ExcelRowDTO> rowsMap;
    public ExcelTrialBalanceExcelRowHelper(Map<TrialBalanceEnum, ExcelRowDTO> rowsMap) {
        this.rowsMap = rowsMap;
    }

    public Double getCredit(TrialBalanceEnum enumKey) {
        ExcelRowDTO row = rowsMap.get(enumKey);
        double credit;
        if (row != null) {
            credit = row.getCredit()!= null ? row.getCredit() : 0.0;
        }else {
            credit = 0.0;
        }
        return credit;
    }

    public Double getDebit(TrialBalanceEnum enumKey) {
        ExcelRowDTO row = rowsMap.get(enumKey);
        double debit;
        if (row != null) {
            debit = row.getDebit() != null ? row.getDebit() : 0.0;
        }else {
            debit = 0.0;
        }
        return debit;
    }
}
