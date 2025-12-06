package com.progress.coolProject.Enums;

public enum AccountType {
    DR("DR", "Debit"),
    CR("CR", "Credit");

    private final String code;
    private final String description;

    AccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AccountType fromCode(String code) {
        for (AccountType type : values()) {
            if (type.code.equalsIgnoreCase(code.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}
