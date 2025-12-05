package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;

public enum LoanType {
    BUSINESS_LOAN("BUSINESS LOAN"),
    EDUCATION_LOAN("EDUCATION LOAN"),
    AGRICULTURAL_LOAN("AGRICULTURAL LOAN"),
    GHAR_LOAN("GHAR LOAN"),
    HEALTH_LOAN("HEALTH LOAN"),
    JIBIKOPARJAN_LOAN("JIBIKOPARJAN LOAN"),
    OTHER_LOAN("OTHERS LOAN"),
    PROVIDENT_FUND_LOAN("PROVIDENT FUND LOAN");

    private final String loanTypeName;

    LoanType(String loanTypeName) {
        this.loanTypeName = loanTypeName;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public static Optional<LoanType> findByLoanTypeName(String loanTypeName) {
        return Arrays.stream(values())
                .filter(tb -> tb.getLoanTypeName().equalsIgnoreCase(loanTypeName.trim()))
                .findFirst();
    }
}
