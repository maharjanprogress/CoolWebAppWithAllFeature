package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;

public enum TrialBalanceEnum {
    SALARY(1001, "Salary", "तलब"),
    TELEPHONE_EXPENSE(1003, "Telephone Expenses", "टेलिफोन खर्च"),
    ELECTRICITY_EXPENSE(1004, "Electricity Expenses", "बिजुली खर्च"),
    WATER_EXPENSE(1005, "Water Expenses", "पानी खर्च"),
    PRINTING_EXPENSE(1007, "Printing Expenses", "छपाइ खर्च"),
    STATIONARY_EXPENSE(1008, "Stationary Expenses", "लेखन सामग्री खर्च"),
    TRAVELLING_EXPENSE(1009, "Travelling Expenses", "यात्रा खर्च"),
    FINE_AND_PENALTY(1013, "Fine & Penalty", "जरिवाना तथा दण्ड"),
    MISCELLANEOUS_INCOME(1023, "Miscellaneous Income", "विविध आय"),
    AUDIT_FEE_EXPENSE(1026, "Audit Fee Expenses", "लेखापरिक्षण शुल्क खर्च"),
    DASHAIN_ALLOWANCE(1028, "Dashain Allowance", "दशैं भत्ता"),
    TIFFIN_EXPENSE(1029, "Tiffin Expenses", "खाजा खर्च"),
    GENERAL_MEETING(1030, "General Meeting Expenses", "साधारण सभा खर्च"),
    REPAIR_AND_MAINTENANCE_CHARGE(1031, "Repair And Maintenance Charge", "मर्मत तथा संभार"),
    TRAINING_CHARGE_EXPENSE(1033, "Training Charge Expenses", "प्रशिक्षण शुल्क खर्च"),
    MISCELLANEOUS_EXPENSE(1035, "Miscellaneous Expenses", "विविध खर्च"),
    BONUS_EXPENCES(1038, "Bonus Expenses", "बोनस खर्च"),
    OFFICE_EXPENSE(1042, "Office Expenses", "कार्यालय खर्च"),
    AGM_EXPENSE(1044, "AGM Expenses", "वार्षिक साधारण सभा खर्च"),
    FINE_AND_PENALTIES_EXPENSES(1045, "Fine And Penalties Expenses", "जरिवाना तथा दण्ड"),
    MEMBERSHIP_FEES_INCOME(1051, "Membership Fees Income", "सदस्यता शुल्क आय"),
    DISCOUNT_INCOME(1053, "Discount Income", "छुट आय"),
    OFFICE_CLEANING(1064, "Office Cleaning Expenses", "कार्यालय सफाइ खर्च"),
    AMC_EXPENSE(1065, "AMC Expenses", "एएमसी खर्च"),
    PASSBOOK_INCOME(1067, "Passbook income", "पासबुक आय"),
    CLOUD_EXPENSE(1068, "Cloud exp", "क्लाउड खर्च"),
    RAHAT_KHARCHA(1070, "Rahat kharcha", "राहत खर्च"),
    CURBS_FEE(1122, "CURBS FEE", "कर्ब्स शुल्क"),
    SHARE_JILLA_SAHAKARI(1414, "SHARE JILLA SAHAKARI SANGH", "शेयर जिल्ला सहकारी संघ"),
    SHARE_NEFSCUN(1415, "SHARE NEFSCUN", "शेयर नेफस्कन"),
    NEFSCUN_REGULAR_SAVING(1416, "NEFSCUN REGULAR SAVING", "नेफ्स्कुन नियमित बचत"),
    SHARE_KASKUN(1417, "Share Kaskun", "शेयर कास्कुन"),
    SANTONA_KHARCHA(1546, "santona kharcha", "सान्तना खर्च"),
    RAHAT_KOSH(1547, "RAHAT KOSH", "राहत कोष"),
    PROVIDENT_EXP(1551, "Provident exp.", "भविष्य कोष खर्च"),
    LAND(2001, "Land", "जग्गा"),
    BUILDING(2002, "Building", "भवन"),
    FURNITURE(2003, "Furniture", "फर्निचर"),
    PRINTER(2005, "Printer", "प्रिन्टर"),
    INTEREST_EXPENDITURE(2010, "Interest Expenditure", "ब्याज खर्च"),
    OFFICE_GOODS(2012, "Office Goods", "कार्यालय सामान"),
    ANYA_JOKHIM_BEWASTHAPAN_KOSH(2021, "Anya Jokhim Bewasthapan Kosh", "अन्य जोखिम व्यवस्थापन कोष"),
    JIBIKOPARCHAN_KOSH(2027, "Jibikoparchan Kosh", "जीविकोपार्जन कोष"),
    RIN_SURAKCHYATA_KOSH(2028, "Rin surakchhyan kosh", "ऋण सुरक्षात्मक कोष"),
    BHAJANGAL_SAHAKARI(2029, "bhajangal sahakari", "भजनगल सहकारी"),
    INTEREST_EXPENDITURE_2(2536, "interest expenditure 2", "ब्याज खर्च"),
    LOAN_ACCOUNT(3001, "Loan Account", "ऋण खाता"),
    OTHER_RISK_MANAGEMENT_FUND(3006, "Other Risk Management Fund", "अन्य जोखिम व्यवस्थापन कोष"),
    RESERVE_FUND(3007, "Reserve Fund", "संचित कोष"),
    BYAG_MULTABI_HISAB(3009, "Byag Multabi Hisab", "ब्याज मुल्ताबी हिसाब"),
    INTEREST_INCOME(3010, "Interest Income", "ब्याज आय"),
    INCOME_TAX_MANAGE(3639, "Income Tax Manage", "आयकर व्यवस्थापन"),
    KRISHI_BIKASH_SAHAKARI_SPECIAL(3641, "Krishi Bikash Sahakari Ltd. Special Tax", "कृषि विकास सहकारी लि. विशेष"),
    KRISHI_BIKASH_SPECIAL_FD_INTEREST(3642, "Krishi Bikash Sahakari Ltd. Special FD Interest", "कृषि विकास सहकारी लि. विशेष एफडी ब्याज"),
    SAVING_ACCOUNT(4001, "Saving Account", "बचत खाता"),
    VAT_EXP(4005,"VAT exp","मूल्य अभिवृद्धि कर खर्च"),
    SANJAL_MEMBER_FEE(4747, "Sanjal member fee", "सञ्जाल सदस्य शुल्क"),
    CASH(5001, "Cash", "नगद"),
    KASKUN_REGULAR_SAVING(5003, "KASKUN REGULAR SAVING", "कास्कुन नियमित"),
    KRISHI_BIKASH_BANK(5005, "KRISHI BIKASH BANK", "कृषि विकास बैंक"),
    SHARE_INVEST_NCBL(5006, "SHARE INVEST NCBL", "शेयर लगानी एनसीई"),
    KASKUN_DAINIK(5007, "KASKUN DAILY SAVING", "कास्कुन दैनिक"),
    BANK_DEVIDEND_SAVING(5008, "bank devident saving", "बैंक लाभांश बचत"),
    NEPAL_INV_BANK(5009, "nepal inv bank", "नेपाल लगानी बैंक"),
    KASKUN_TIME_SAVING(5011, "kaskun time saving", "कास्कुन मियादी बचत"),
    SAMRACHHIT_PUJI_KOSH(5013, "Samrachhit Pugi Kosh", "संरक्षित पूँजी कोष"),
    SAHAKARI_PRABHANDA_KOSH(5014, "Sahakari Prabhanda Kosh", "सहकारी प्रबन्ध कोष"),
    SAHAKARI_SIKCHYA_KOSH(5015, "Sahakari Sikchya kosh", "सहकारी शिक्षा कोष"),
    SAHAKARI_BIKASH_KOSH(5016, "Sahakari Bikash Kosh", "सहकारी विकास कोष"),
    GHATA_PURTI_KOSH(5017, "Ghata Purti Kosh", "घाटा पूर्ति कोष"),
    SAMUDAIYIK_BIKASH_KOSH(5018, "Samudayik Bikash Kosh", "सामुदायिक विकास कोष"),
    STHIRKARAN_KOSH(5019, "Sthirikaran Kosh", "स्थिरकरण कोष"),
    DUBANTI_KOSH(5020, "Dubanti Kosh", "दुबन्ती कोष"),
    SANIMA_BANK(5025, "Sanima Bank", "सानिमा बैंक"),
    RSB_TIME_SAVING(5027, "RSB TIME SAVING", "आरएसके बैंकिङ"),
    PUGIGAT_JAGAEDA_KOSH(5858, "Pugigat Jagaeda Kosh", "पूंजीगत जगेडा कोष"),
    SAMAN_MAUJDAT(5859, "Saman Maujdat", "सामान मौज्दात"),
    INTEREST_TAX(6001, "Interest Tax", "ब्याज कर"),
    OTHER_PAYABLE(6666, "Other Payable", "अन्य तिर्नुपर्ने"),
    SALARY_PAYABLE(7001, "Salary Payable", "तलब तिर्नुपर्ने"),
    AUDIT_FEE_PAYABLE(7003, "Audit Fee Payable", "लेखापरीक्षण शुल्क तिर्नुपर्ने"),
    TDS_SOCIAL_SECURITY_TAX(7004, "TDS - Social Security Tax", "टीडीएस - सामाजिक सुरक्षा कर"),
    TDS_AUDIT_FEE(7006, "TDS - Audit Fee", "टीडीएस - लेखापरीक्षण शुल्क"),
    BAITHAK_BHATTA_TAX(7007, "baithak bhatta tax", "बैठक भत्ता कर"),
    TDS_PAYABLE(7008, "TDS - Payable", "टीडीएस तिर्नुपर्ने"),
    DRESS(7330, "dress", "पोशाक"),
    LOAN_LOSS_PROVISION_EXPENSES(9901, "Loan Loss Provision Expenses", "ऋण घाटा प्रावधान खर्च"),
    ACCOUNT_CLOSING_CHARGE(9005, "Account Closing Charge", "खाता बन्द शुल्क"),
    SHARE_CAPITAL(9009, "Share Capital", "शेयर पूंजी"),
    SECURED_CAPITAL_REDEMPTION_FUND(9010, "Secured Capital Redemption Fund (Patronage Fund)", "सुरक्षित पूंजी मोचन कोष"),
    EMPLOYEE_BONUS_FUND(9011, "Employee Bonus Fund", "कर्मचारी बोनस कोष"),
    COOPERATIVE_EDUCATION_FUND(9012, "Co-operative Education Fund", "सहकारी शिक्षा कोष"),
    COOPERATIVE_DEVELOPMENT_FUND(9013, "Co-operative Development Fund", "सहकारी विकास कोष"),
    LOSS_FULFILMENT_FUND(9014, "Loss Fulfilment Fund", "घाटा पूर्ति कोष"),
    LOAN_LOSS_PROVISION(9015, "Loan Loss Provision", "ऋण घाटा प्रावधान"),
    COOPERATIVE_PROMOTION_FUND(9016, "Cooperative Promotion Fund", "सहकारी प्रवर्धन कोष"),
    INCOME_TAX_PAYABLE(9031, "income tax payble", "तिर्नु पर्ने आयकर"),
    GHAR_JAGGA_TAX(9034, "ghar jagga tax", "घर जग्गा कर"),
    INTEREST_PAYABLE(9036, "Interest Payable", "ब्याज तिर्नुपर्ने"),
    OTHER_LIABILITIES(9038, "Other Liabilities", "अन्य दायित्व"),
    NATIONAL_COOPERATIVE(9040, "National Co-operative Bank Ltd.", "राष्ट्रिय सहकारी"),
    ADVANCES_RECEIVABLES(9057, "Advances Receivables", "अग्रिम प्राप्ति"),
    TDS_RECEIVABLES_ADVANCE_TAX(9063, "TDS Receivables & Advance Tax", "टीडीएस प्राप्य र अग्रिम कर"),
    LAND_AND_BUILDING(9066, "Land & Building", "जग्गा तथा भवन"),
    OFFICE_EQUIPMENTS(9067, "Office Equipments", "कार्यालय उपकरण"),
    SOCIAL_PROGRAMME(9088, "Social Programme Expenses", "सामाजिक कार्यक्रम"),
    SERVICE_CHARGE(9905, "Service Charge", "सेवा शुल्क"),
    CURRENT_YEAR_PROFIT_LOSS(9990, "Current Year Profit & Loss", "चालू वर्ष नाफा नोक्सान"),
    SHARE_DIVIDEND_FUND(9991, "Share Dividend Fund", "शेयर लाभांश कोष"),
    TDS_ON_INTEREST_RECEIVABLE(9996, "TDS On Interest Receivable", "ब्याज प्राप्यमा टीडीएस"),
    BANK_INTEREST_INCOME(9997, "Bank Interest Income", "बैंक ब्याज आय"),
    OPENING_DIFFERENCE(9998, "Opening Difference", "सुरुवात फरक");

    private final int ledgerNo;
    private final String descriptionEn;
    private final String descriptionNp;

    TrialBalanceEnum(int ledgerNo, String descriptionEn, String descriptionNp) {
        this.ledgerNo = ledgerNo;
        this.descriptionEn = descriptionEn;
        this.descriptionNp = descriptionNp;
    }

    public int getLedgerNo() {
        return ledgerNo;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public String getDescriptionNp() {
        return descriptionNp;
    }

    // Find by ledger number
    public static Optional<TrialBalanceEnum> findByLedgerNo(int ledgerNo) {
        return Arrays.stream(values())
                .filter(tb -> tb.getLedgerNo() == ledgerNo)
                .findFirst();
    }

    // Find by English description (case-insensitive, trimmed)
    public static Optional<TrialBalanceEnum> findByDescriptionEn(String description) {
        if (description == null) return Optional.empty();
        String normalized = description.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(tb -> tb.getDescriptionEn().toLowerCase().equals(normalized))
                .findFirst();
    }
}