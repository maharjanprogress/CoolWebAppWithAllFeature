package com.progress.coolProject.Utils.PowerPoint;

import lombok.experimental.UtilityClass;
import org.apache.poi.sl.usermodel.TextParagraph;
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
}
