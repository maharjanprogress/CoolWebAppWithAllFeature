package com.progress.coolProject.Utils.PowerPoint;

import lombok.experimental.UtilityClass;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xddf.usermodel.text.XDDFTextRun;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

@UtilityClass
public final class PPTUtils {
    public static void makeTitleForSlide(XSLFSlide slide, String slideTitle) {
        XSLFTextBox slideTitleBox = slide.createTextBox();
        slideTitleBox.setAnchor(new Rectangle(0, 0, 720, 50));
        slideTitleBox.setFillColor(new Color(255, 204, 102)); // Yellow background

        XSLFTextParagraph slideTitlePara = slideTitleBox.addNewTextParagraph();
        slideTitlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun slideTitleRun = slideTitlePara.addNewTextRun();
        slideTitleRun.setText(slideTitle);
        slideTitleRun.setFontSize(18.0);
        slideTitleRun.setBold(true);
        slideTitleRun.setFontColor(Color.BLACK);
    }

    public static void setCellTextWithStyle(XSLFTableCell cell,
                                             String text, TextAlignment alignment, PresetColor textColor,
                                             double fontSize, boolean isBold,
                                             Color bgColor, Color borderColor) {
        if (text != null) {
            cell.setText(text);
        }
        cell.setFillColor(bgColor);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Add borders to all sides
        cell.setBorderWidth(TableCell.BorderEdge.top, 1.0);
        cell.setBorderWidth(TableCell.BorderEdge.bottom, 1.0);
        cell.setBorderWidth(TableCell.BorderEdge.left, 1.0);
        cell.setBorderWidth(TableCell.BorderEdge.right, 1.0);

        // Set border color (black)
        cell.setBorderColor(TableCell.BorderEdge.top, borderColor);
        cell.setBorderColor(TableCell.BorderEdge.bottom, borderColor);
        cell.setBorderColor(TableCell.BorderEdge.left, borderColor);
        cell.setBorderColor(TableCell.BorderEdge.right, borderColor);

        XDDFTextParagraph para = cell.getTextBody().getParagraphs().getFirst();
        if (alignment != null){
            para.setTextAlignment(alignment);
        }

        XDDFTextRun run = para.getTextRuns().getFirst();
        run.setBold(isBold);
        run.setFontSize(fontSize);
        run.setFontColor(XDDFColor.from(textColor));
    }
}
