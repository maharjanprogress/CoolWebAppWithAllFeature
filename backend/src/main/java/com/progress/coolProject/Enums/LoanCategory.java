package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;

public enum LoanCategory {
    AGRICULTURAL_LOAN(301, "AGRICULTURAL LOAN"),
    BUSINESS_LOAN(302, "BUSINESS LOAN"),
    EDUCATION_LOAN(303, "EDUCATION LOAN"),
    GHAR_LOAN(304, "GHAR LOAN"),
    HEALTH_LOAN(305, "HEALTH LOAN"),
    JIBIKOPARJAN_LOAN(306, "JIBIKOPARJAN LOAN"),
    OTHERS_LOAN(307, "OTHERS LOAN"),
    PROVIDENT_FUND_LOAN(308, "PROVIDENT FUND LOAN");

    private final String loanTypeName;
    private final int code;

    LoanCategory(int code, String loanTypeName) {
        this.loanTypeName = loanTypeName;
        this.code = code;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public int getCode() {
        return code;
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
