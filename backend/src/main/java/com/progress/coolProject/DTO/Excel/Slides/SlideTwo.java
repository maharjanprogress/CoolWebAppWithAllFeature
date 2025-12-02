package com.progress.coolProject.DTO.Excel.Slides;

import com.progress.coolProject.StringConstants;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.experimental.UtilityClass;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

@UtilityClass
public final class SlideTwo {
    public static final String FIRST_ROW_TITLE = StringConstants.CURRENT_YEAR+" "+ StringConstants.SHRAWAN +" देखि "+ StringConstants.KARTIK +" मसान्तसम्मको नाफा नोक्सान हिसाब विवरण";

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle, ExcelTrialBalanceExcelRowHelper data) {
    }
}
