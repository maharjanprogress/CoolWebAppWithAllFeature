package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.experimental.UtilityClass;
import org.apache.poi.xddf.usermodel.PresetColor;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Utils.Excel.ExcelLoanAgeingHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xssf.usermodel.TextAlign;

import java.awt.*;

@UtilityClass
public class SlideFourtyOne {

    // Column Headers
    public static final String COLUMN_INDICATOR = "संकेतहरु";
    public static final String COLUMN_EMPTY_1 = "";
    public static final String COLUMN_EMPTY_2 = "";
    public static final String COLUMN_MEASURE = "हुनुपर्ने (लक्ष्य)";
    public static final String COLUMN_Q1 = "पहिलो त्रैमासिक";
    public static final String COLUMN_Q2 = "दोश्रो त्रैमासिक";
    public static final String COLUMN_Q3 = "तेश्रो त्रैमासिक";
    public static final String COLUMN_Q4 = "चौथो त्रैमासिक";

    // Row Labels
    public static final String ROW_A_BAD = "पि वान";
    public static final String ROW_A_BAD_DETAIL = "खराब कर्जा";
    public static final String ROW_A_BAD_DESC = "१२ महिना भन्दाबढी भाखा नामचेको ऋण";

    public static final String ROW_B_DOUBTFUL = "पि डु";
    public static final String ROW_B_DOUBTFUL_DETAIL = "शंकास्पद कर्जा";
    public static final String ROW_B_DOUBTFUL_DESC = "६-१२ महीनासम्म भाखा नामचेको ऋण";

    public static final String ROW_C_SUBSTANDARD = "पि टु यक्स";
    public static final String ROW_C_SUBSTANDARD_DETAIL = "कमसल कर्जा";
    public static final String ROW_C_SUBSTANDARD_DESC = "३-६ महीनासम्म भाखा नामचेको ऋण";

    public static final String ROW_D_WATCHLIST = "पि टु यक्स";
    public static final String ROW_D_WATCHLIST_DETAIL = "असल कर्जा";
    public static final String ROW_D_WATCHLIST_DESC = "भाखा नामचेको ऋण";

    private static final double NORMAL_FONT_SIZE = 17.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper,
                                       ExcelTrialBalanceExcelRowHelper data) {

        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(0);
        nepaliFormat.setMaximumFractionDigits(0);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 7 columns
        // 1 header row + 4 data rows (with 3 sub-rows each) = 13 rows total
        int numRows = 5;
        int numCols = 8;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 60, 700, 500));

        // Set column widths
        table.setColumnWidth(0, 80);   // Indicator
        table.setColumnWidth(1, 100);  // Empty/Detail
        table.setColumnWidth(2, 140);  // Measure (Description)
        table.setColumnWidth(3, 76);   // Q1
        table.setColumnWidth(4, 76);   // Q1
        table.setColumnWidth(5, 76);   // Q2
        table.setColumnWidth(6, 76);   // Q3
        table.setColumnWidth(7, 76);   // Q4

        // Style for header cells (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // Header row (row 0)
        String[] headers = {
                COLUMN_INDICATOR, COLUMN_EMPTY_1, COLUMN_EMPTY_2, COLUMN_MEASURE,
                COLUMN_Q1, COLUMN_Q2, COLUMN_Q3, COLUMN_Q4
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    18.0, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // Data colors
        Color lightOrange = new Color(255, 217, 194);
        Color lightBlue = new Color(217, 225, 242);
        Color lightGreen = new Color(227, 239, 217);
        Color lightGray = new Color(242, 242, 242);

        // Determine which quarters to fill
        Traimasik currentQuarter = Traimasik.getCurrent();
        Traimasik[] quartersToFill = currentQuarter.getIncludingPrevious();

        // Row 1-3: Bad Loan (खराब ऋण) - RED/ORANGE
        fillLoanCategoryRows(table, 1, ROW_A_BAD, ROW_A_BAD_DETAIL, ROW_A_BAD_DESC,
                "१००%", quartersToFill, nepaliFormat, lightOrange);

        // Row 4-6: Doubtful Loan (शंकास्पद ऋण) - BLUE
        fillLoanCategoryRows(table, 2, ROW_B_DOUBTFUL, ROW_B_DOUBTFUL_DETAIL, ROW_B_DOUBTFUL_DESC,
                "५०%", quartersToFill, nepaliFormat, lightBlue);

        // Row 7-9: Substandard Loan (कमसल ऋण) - GREEN
        fillLoanCategoryRows(table, 3, ROW_C_SUBSTANDARD, ROW_C_SUBSTANDARD_DETAIL, ROW_C_SUBSTANDARD_DESC,
                "२५%", quartersToFill, nepaliFormat, lightGreen);

        // Row 10-12: Watch List Loan (असल ऋण) - GRAY
        fillLoanCategoryRows(table, 4, ROW_D_WATCHLIST, ROW_D_WATCHLIST_DETAIL, ROW_D_WATCHLIST_DESC,
                "१%", quartersToFill, nepaliFormat, lightGray);
    }

    /**
     * Fill a 3-row loan category section
     */
    private static void fillLoanCategoryRows(XSLFTable table, int startRow,
                                             String indicator, String detail, String description,
                                             String targetPercentage,
                                             Traimasik[] quartersToFill,
                                             NumberFormat formatter,
                                             Color bgColor) {
        // Row 1: Indicator + Detail + Description + Target + Quarterly percentages
        fillCellWithStyle(table, startRow, 0, indicator, TextAlignment.LEFT, bgColor);
        fillCellWithStyle(table, startRow, 1, detail, TextAlignment.LEFT, bgColor);
        fillCellWithStyle(table, startRow, 2, description, TextAlignment.LEFT, bgColor);
        fillCellWithStyle(table, startRow, 3, targetPercentage, TextAlignment.CENTER, bgColor);

        // Fill quarterly data based on which quarters should be shown
        for (int col = 4; col < 8; col++) {
            int quarterIndex = col - 3; // Q1=1, Q2=2, Q3=3, Q4=4
            boolean shouldFill = false;

            for (Traimasik q : quartersToFill) {
                if (q.ordinal() + 1 == quarterIndex) {
                    shouldFill = true;
                    break;
                }
            }

            String value = shouldFill ? targetPercentage : "";
            fillCellWithStyle(table, startRow, col, value, TextAlignment.CENTER, bgColor);
        }
    }

    private static void fillCellWithStyle(XSLFTable table, int row, int col,
                                          String text, TextAlignment alignment, Color bgColor) {
        XSLFTableCell cell = table.getCell(row, col);
        PPTUtils.setCellTextWithStyle(cell,
                text, alignment, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}
