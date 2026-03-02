package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoanCategory {
    AGRICULTURAL_LOAN(301, "AGRICULTURAL LOAN","AL"),
    BUSINESS_LOAN(302, "BUSINESS LOAN","BL"),
    EDUCATION_LOAN(303, "EDUCATION LOAN","EL"),
    GHAR_LOAN(304, "GHAR LOAN","GL"),
    HEALTH_LOAN(305, "HEALTH LOAN","HL"),
    JIBIKOPARJAN_LOAN(306, "JIBIKOPARJAN LOAN","JL"),
    OTHERS_LOAN(307, "OTHERS LOAN","OL"),
    PROVIDENT_FUND_LOAN(308, "PROVIDENT FUND LOAN","PL");

    private final String loanTypeName;
    private final int code;
    private final String shortDescription;

    LoanCategory(int code, String loanTypeName, String shortDescription) {
        this.loanTypeName = loanTypeName;
        this.code = code;
        this.shortDescription = shortDescription;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public int getCode() {
        return code;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public static Optional<LoanCategory> findByAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return Optional.empty();
        }

        Pattern pattern = Pattern.compile("([A-Z]{2})-\\d+$");
        Matcher matcher = pattern.matcher(accountNumber);

        if (!matcher.find()) {
            return Optional.empty();
        }

        String shortDesc = matcher.group(1);

        return Arrays.stream(values())
                .filter(sc -> sc.getShortDescription().equalsIgnoreCase(shortDesc))
                .filter(sc -> accountNumber.contains(String.valueOf(sc.getCode())))
                .findFirst();
    }

    public static Optional<LoanCategory> findByLoanTypeName(String loanTypeName) {
        return Arrays.stream(values())
                .filter(tb -> tb.getLoanTypeName().equalsIgnoreCase(loanTypeName.trim()))
                .findFirst();
    }

    public static Optional<LoanCategory> findByCode(int code) {
        return Arrays.stream(values())
                .filter(lc -> lc.getCode() == code)
                .findFirst();
    }
}
