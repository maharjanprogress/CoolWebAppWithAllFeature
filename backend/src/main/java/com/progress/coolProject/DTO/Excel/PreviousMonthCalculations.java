package com.progress.coolProject.DTO.Excel;

import com.progress.coolProject.DTO.Excel.Slides.SlideThirteen;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelLoanAgeingHelper;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.Getter;


@Getter
public class PreviousMonthCalculations {
    private final double previousMonthShareCapital;
    private final double previousMonthTotalBalanceSheetCredit;
    private final double previousMonthLoanAccount;
    private final double previousMonthTotalLoan;
    private final double previousLLP;

    public PreviousMonthCalculations(ExcelTrialBalanceExcelRowHelper previousMonthBalanceSheet, ExcelLoanAgeingHelper previousMonthLoanAgeing){
        this.previousMonthShareCapital = previousMonthBalanceSheet.getCredit(TrialBalanceEnum.SHARE_CAPITAL);
        this.previousMonthTotalBalanceSheetCredit = previousMonthBalanceSheet.getTotalCredit();
        this.previousMonthLoanAccount = previousMonthBalanceSheet.getDebit(TrialBalanceEnum.LOAN_ACCOUNT);
        this.previousMonthTotalLoan = previousMonthLoanAgeing.getTotalBalance();
        this.previousLLP = SlideThirteen.getTotalAffectedRiskAmount(previousMonthLoanAgeing);
    }

    public double getKhudRin(){
        return this.previousMonthTotalLoan - this.previousLLP;
    }
}
