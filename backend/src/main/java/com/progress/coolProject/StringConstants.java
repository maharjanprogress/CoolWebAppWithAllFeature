package com.progress.coolProject;

public class StringConstants {
    public static final String FILE_STORAGE_PATH = "/opt/coolwebapp/";
    public static final String FILE_ACCESS_URL = "/coolwebapp/";


    public static final String CURRENT_YEAR = "२०८२";

    public static final String BAISAKH = "बैशाख";
    public static final String JESTHA = "जेठ";
    public static final String ASAR = "असार";
    public static final String SHRAWAN = "श्रमावन";
    public static final String BHADRA = "भदौ";
    public static final String ASHOJ = "असोज";
    public static final String KARTIK = "कार्तिक";
    public static final String MANGSIR = "मंसिर";
    public static final String POUS = "पुष";
    public static final String MAGH = "माघ";
    public static final String FALGUN = "फागुन";
    public static final String CHAITRA = "चैत";

    public static String getMonths(String... months){
        StringBuilder sb = new StringBuilder();
        for (String month : months) {
            sb.append(month);
            sb.append(" ,");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
