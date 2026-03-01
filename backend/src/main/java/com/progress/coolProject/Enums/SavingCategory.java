package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SavingCategory {
    BAL_BACHAT(201, "BAL BACHAT","BB"),
    DAILY_SAVING(202, "DAILY SAVING", "DS"),
    KHUTRUKAE_BACHAT(203, "KHUTRUKAE BACHAT", "KB"),
    MONTHLY_SAVING(204, "MONTHLY SAVINIG", "MS"), // Note: Typo in original "SAVINIG"
    PROVIDENT_FUND_SAVING(205, "PROVIDENT FUND SAVING", "PF"),
    RAHAT_BACHAT(206, "RAHAT BACHAT", "RB"),
    TIME_SAVING(207, "TIME SAVING", "TS"),
    WEEKLY_SAVING(208, "WEEKLY SAVING", "WS"),
    JESTHA_NAGARIKA_BACHAT(209, "JESTHA NAGARIKA BACHAT", "JB");

    private final int code;
    private final String description;
    private final String shortDescription;

    SavingCategory(int code, String description, String shortDescription) {
        this.code = code;
        this.description = description;
        this.shortDescription = shortDescription;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public static Optional<SavingCategory> findByCode(int code) {
        return Arrays.stream(values())
                .filter(sc -> sc.getCode() == code)
                .findFirst();
    }

    public static Optional<SavingCategory> findByAccountNumber(String accountNumber) {
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

    public static Optional<SavingCategory> findByDescription(String description) {
        return Arrays.stream(values())
                .filter(sc -> sc.getDescription().equalsIgnoreCase(description.trim()))
                .findFirst();
    }
}
