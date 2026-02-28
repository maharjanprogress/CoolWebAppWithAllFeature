package com.progress.coolProject.DTO.Excel.Slides;


import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import com.progress.coolProject.Utils.date.NepaliDate;
import lombok.experimental.UtilityClass;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;

import java.awt.*;

@UtilityClass
public final class SlideFourteen {
    // Header
    public static final String FIRST_ROW_TITLE = "१३. " + Traimasik.getTraimasikForMonth(NepaliDate.now().plusMonths(-1).getMonth()).getLocale() + " त्रैमासिक प्रगति समिक्षा ("+ Traimasik.getmonthBetweenTraimasikofPlusmonthConcatenated(-1) +") " + NepaliDate.now().plusMonths(-1).getYearInNepali();
    public static final String SECOND_ROW_SUBTITLE = "• सञ्चालक समिति, लेखा, उपसमिति र कर्मचारीहरुको बैठक (वार्षिक बजेटमा आधारित)";

    // Column Headers
    public static final String COLUMN_COMMITTEE_SUBCOMMITTEE = "समिति, उपसमिति";
    public static final String COLUMN_ANNUAL_TARGET = "बार्षिक लक्ष";
    public static final String COLUMN_QUARTERLY_TARGET = "पहिलो त्रैमासिक लक्ष";
    public static final String COLUMN_AVAILABLE = "उपलब्धी";
    public static final String COLUMN_PERCENTAGE = "प्रतिशत";
    public static final String COLUMN_COUNT = "संख्या";
    public static final String COLUMN_ATTENDANCE_PERCENTAGE = "उपस्थिति प्रतिशत";

    // Row Labels
    public static final String ROW_BOARD_COMMITTEE = "सञ्चालक";
    public static final String ROW_AUDIT = "लेखा";
    public static final String ROW_LOAN_SUBCOMMITTEE = "ऋणे उपसमिति";
    public static final String ROW_EDUCATION_SUBCOMMITTEE = "शिक्षा उपसमिति";
    public static final String ROW_HEALTH_SUBCOMMITTEE = "स्वास्थ्य उपसमिति";
    public static final String ROW_EMPLOYEES = "कर्मचारी";

    private static final double NORMAL_FONT_SIZE = 22.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Board Committee Calculation Methods
    public static Integer getBoardCommitteeAnnualTarget() {
        // TODO: Add calculation - annual meeting target for board committee
        return 0;
    }

    public static Integer getBoardCommitteeQuarterlyTarget() {
        // TODO: Add calculation - quarterly meeting target
        return 0;
    }

    public static Integer getBoardCommitteeAvailable() {
        // TODO: Add calculation - actual meetings held
        return 0;
    }

    public static Double getBoardCommitteePercentage() {
        // TODO: Add calculation - (available / quarterly target) * 100
        return 0.0;
    }

    public static Integer getBoardCommitteeCount() {
        // TODO: Add calculation - number of board committee members
        return 0;
    }

    public static Double getBoardCommitteeAttendance() {
        // TODO: Add calculation - attendance percentage
        return 0.0;
    }

    // Audit Committee Calculation Methods
    public static Integer getAuditAnnualTarget() {
        return 0;
    }

    public static Integer getAuditQuarterlyTarget() {
        return 0;
    }

    public static Integer getAuditAvailable() {
        return 0;
    }

    public static Double getAuditPercentage() {
        return 0.0;
    }

    public static Integer getAuditCount() {
        return 0;
    }

    public static Double getAuditAttendance() {
        return 0.0;
    }

    // Loan Subcommittee Calculation Methods
    public static Integer getLoanSubcommitteeAnnualTarget() {
        return 0;
    }

    public static Integer getLoanSubcommitteeQuarterlyTarget() {
        return 0;
    }

    public static Integer getLoanSubcommitteeAvailable() {
        return 0;
    }

    public static Double getLoanSubcommitteePercentage() {
        return 0.0;
    }

    public static Integer getLoanSubcommitteeCount() {
        return 0;
    }

    public static Double getLoanSubcommitteeAttendance() {
        return 0.0;
    }

    // Education Subcommittee Calculation Methods
    public static Integer getEducationSubcommitteeAnnualTarget() {
        return 0;
    }

    public static Integer getEducationSubcommitteeQuarterlyTarget() {
        return 0;
    }

    public static Integer getEducationSubcommitteeAvailable() {
        return 0;
    }

    public static Double getEducationSubcommitteePercentage() {
        return 0.0;
    }

    public static Integer getEducationSubcommitteeCount() {
        return 0;
    }

    public static Double getEducationSubcommitteeAttendance() {
        return 0.0;
    }

    // Health Subcommittee Calculation Methods
    public static Integer getHealthSubcommitteeAnnualTarget() {
        return 0;
    }

    public static Integer getHealthSubcommitteeQuarterlyTarget() {
        return 0;
    }

    public static Integer getHealthSubcommitteeAvailable() {
        return 0;
    }

    public static Double getHealthSubcommitteePercentage() {
        return 0.0;
    }

    public static Integer getHealthSubcommitteeCount() {
        return 0;
    }

    public static Double getHealthSubcommitteeAttendance() {
        return 0.0;
    }

    // Employees Calculation Methods
    public static Integer getEmployeesAnnualTarget() {
        return 0;
    }

    public static Integer getEmployeesQuarterlyTarget() {
        return 0;
    }

    public static Integer getEmployeesAvailable() {
        return 0;
    }

    public static Double getEmployeesPercentage() {
        return 0.0;
    }

    public static Integer getEmployeesCount() {
        return 0;
    }

    public static Double getEmployeesAttendance() {
        return 0.0;
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Add subtitle text box
        org.apache.poi.xslf.usermodel.XSLFTextBox subtitleBox = slide.createTextBox();
        subtitleBox.setAnchor(new Rectangle(20, 100, 680, 30));
        org.apache.poi.xslf.usermodel.XSLFTextParagraph subtitlePara = subtitleBox.addNewTextParagraph();
        subtitlePara.setTextAlign(org.apache.poi.sl.usermodel.TextParagraph.TextAlign.LEFT);
        org.apache.poi.xslf.usermodel.XSLFTextRun subtitleRun = subtitlePara.addNewTextRun();
        subtitleRun.setText(SECOND_ROW_SUBTITLE);
        subtitleRun.setFontSize(12.0);
        subtitleRun.setFontColor(Color.BLACK);

        // Create table structure: 7 columns
        // 1 header row + 6 data rows = 7 rows
        int numRows = 7;
        int numCols = 7;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 140, 700, 360));

        // Set column widths
        table.setColumnWidth(0, 140); // Committee/Subcommittee
        table.setColumnWidth(1, 110);  // Annual Target
        table.setColumnWidth(2, 110);  // Quarterly Target
        table.setColumnWidth(3, 80);  // Available
        table.setColumnWidth(4, 80);  // Percentage
        table.setColumnWidth(5, 70);  // Count
        table.setColumnWidth(6, 110);  // Attendance %

        // Style for header cells (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // Header row (row 0)
        String[] headers = {
                COLUMN_COMMITTEE_SUBCOMMITTEE, COLUMN_ANNUAL_TARGET, COLUMN_QUARTERLY_TARGET,
                COLUMN_AVAILABLE, COLUMN_PERCENTAGE, COLUMN_COUNT, COLUMN_ATTENDANCE_PERCENTAGE
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    9.5, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        // Row 1: Board Committee (सञ्चालक)
        fillDataRow(table, 1, ROW_BOARD_COMMITTEE,
                getBoardCommitteeAnnualTarget(),
                getBoardCommitteeQuarterlyTarget(),
                getBoardCommitteeAvailable(),
                getBoardCommitteePercentage(),
                getBoardCommitteeCount(),
                getBoardCommitteeAttendance(),
                nepaliFormat, lightBlue);

        // Row 2: Audit (लेखा)
        fillDataRow(table, 2, ROW_AUDIT,
                getAuditAnnualTarget(),
                getAuditQuarterlyTarget(),
                getAuditAvailable(),
                getAuditPercentage(),
                getAuditCount(),
                getAuditAttendance(),
                nepaliFormat, white);

        // Row 3: Loan Subcommittee (ऋणे उपसमिति)
        fillDataRow(table, 3, ROW_LOAN_SUBCOMMITTEE,
                getLoanSubcommitteeAnnualTarget(),
                getLoanSubcommitteeQuarterlyTarget(),
                getLoanSubcommitteeAvailable(),
                getLoanSubcommitteePercentage(),
                getLoanSubcommitteeCount(),
                getLoanSubcommitteeAttendance(),
                nepaliFormat, lightBlue);

        // Row 4: Education Subcommittee (शिक्षा उपसमिति)
        fillDataRow(table, 4, ROW_EDUCATION_SUBCOMMITTEE,
                getEducationSubcommitteeAnnualTarget(),
                getEducationSubcommitteeQuarterlyTarget(),
                getEducationSubcommitteeAvailable(),
                getEducationSubcommitteePercentage(),
                getEducationSubcommitteeCount(),
                getEducationSubcommitteeAttendance(),
                nepaliFormat, white);

        // Row 5: Health Subcommittee (स्वास्थ्य उपसमिति)
        fillDataRow(table, 5, ROW_HEALTH_SUBCOMMITTEE,
                getHealthSubcommitteeAnnualTarget(),
                getHealthSubcommitteeQuarterlyTarget(),
                getHealthSubcommitteeAvailable(),
                getHealthSubcommitteePercentage(),
                getHealthSubcommitteeCount(),
                getHealthSubcommitteeAttendance(),
                nepaliFormat, lightBlue);

        // Row 6: Employees (कर्मचारी)
        fillDataRow(table, 6, ROW_EMPLOYEES,
                getEmployeesAnnualTarget(),
                getEmployeesQuarterlyTarget(),
                getEmployeesAvailable(),
                getEmployeesPercentage(),
                getEmployeesCount(),
                getEmployeesAttendance(),
                nepaliFormat, white);
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String committeeName,
                                    Integer annualTarget, Integer quarterlyTarget,
                                    Integer available, Double percentage,
                                    Integer count, Double attendancePercentage,
                                    NumberFormat formatter, Color bgColor) {
        // Column 0: Committee/Subcommittee Name
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                committeeName, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 1: Annual Target
        XSLFTableCell cell1 = table.getCell(row, 1);
        String annualText = (annualTarget != null) ? formatter.format(annualTarget) : "";
        PPTUtils.setCellTextWithStyle(cell1,
                annualText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 2: Quarterly Target
        XSLFTableCell cell2 = table.getCell(row, 2);
        String quarterlyText = (quarterlyTarget != null) ? formatter.format(quarterlyTarget) : "";
        PPTUtils.setCellTextWithStyle(cell2,
                quarterlyText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 3: Available
        XSLFTableCell cell3 = table.getCell(row, 3);
        String availableText = (available != null) ? formatter.format(available) : "";
        PPTUtils.setCellTextWithStyle(cell3,
                availableText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 4: Percentage
        XSLFTableCell cell4 = table.getCell(row, 4);
        String percentText = (percentage != null) ? formatter.format(percentage) : "";
        PPTUtils.setCellTextWithStyle(cell4,
                percentText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 5: Count
        XSLFTableCell cell5 = table.getCell(row, 5);
        String countText = (count != null) ? formatter.format(count) : "";
        PPTUtils.setCellTextWithStyle(cell5,
                countText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 6: Attendance Percentage
        XSLFTableCell cell6 = table.getCell(row, 6);
        String attendanceText = (attendancePercentage != null) ? formatter.format(attendancePercentage) : "";
        PPTUtils.setCellTextWithStyle(cell6,
                attendanceText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}
