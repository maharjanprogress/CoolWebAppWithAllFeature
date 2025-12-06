package com.progress.coolProject.Enums;

import java.util.Arrays;
import java.util.Optional;

public enum SavingCategory {
    BAL_BACHAT(201, "BAL BACHAT"),
    DAILY_SAVING(202, "DAILY SAVING"),
    JESTHA_NAGARIKA_BACHAT(209, "JESTHA NAGARIKA BACHAT"),
    KHUTRUKAE_BACHAT(203, "KHUTRUKAE BACHAT"),
    MONTHLY_SAVING(204, "MONTHLY SAVINIG"), // Note: Typo in original "SAVINIG"
    PROVIDENT_FUND_SAVING(205, "PROVIDENT FUND SAVING"),
    RAHAT_BACHAT(206, "RAHAT BACHAT"),
    TIME_SAVING(207, "TIME SAVING"),
    WEEKLY_SAVING(208, "WEEKLY SAVING");

    private final int code;
    private final String description;

    SavingCategory(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<SavingCategory> findByCode(int code) {
        return Arrays.stream(values())
                .filter(sc -> sc.getCode() == code)
                .findFirst();
    }

    public static Optional<SavingCategory> findByDescription(String description) {
        return Arrays.stream(values())
                .filter(sc -> sc.getDescription().equalsIgnoreCase(description.trim()))
                .findFirst();
    }
}
