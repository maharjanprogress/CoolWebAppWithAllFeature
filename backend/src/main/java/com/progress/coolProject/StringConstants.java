package com.progress.coolProject;

public class StringConstants {
    public static final String FILE_STORAGE_PATH = "/opt/coolwebapp/";
    public static final String FILE_ACCESS_URL = "/coolwebapp/";

    public static final String[] months = {
            "Baisakh", "Jestha", "Ashadh", "Shrawan",
            "Bhadra", "Ashwin", "Kartik", "Mangsir",
            "Poush", "Magh", "Falgun", "Chaitra"
    };

    public static final String[] nepaliMonths = {
            "बैशाख", "जेष्ठ", "आषाढ", "श्रावण",
            "भाद्र", "आश्विन", "कार्तिक", "मंसिर",
            "पौष", "माघ", "फाल्गुन", "चैत्र"
    };

    public static String getMonthsConcatenated(int[] monthIndexes){
        StringBuilder sb = new StringBuilder();
        for (int month : monthIndexes) {
            sb.append(months[month-1]);
            sb.append(" ,");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
