package com.progress.coolProject.Utils.date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NepaliDate implements Comparable<NepaliDate> {
    private int year;
    private int month;
    private int day;

    // Nepali calendar data: days in each month for years 2000 BS to 2099 BS
    private static final int[][] NEPALI_CALENDAR = {
            // Year 2000 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2001 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2002 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2003 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2004 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2005 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2006 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2007 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2008 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            // Year 2009 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2010 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2011 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2012 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2013 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2014 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2015 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2016 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2017 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2018 BS
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2019 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2020 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2021 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2022 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            // Year 2023 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2024 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2025 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2026 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2027 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2028 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2029 BS
            {31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            // Year 2030 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2031 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2032 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2033 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2034 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2035 BS
            {30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            // Year 2036 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2037 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2038 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2039 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2040 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2041 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2042 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2043 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2044 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2045 BS
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2046 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2047 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2048 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2049 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            // Year 2050 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2051 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2052 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2053 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            // Year 2054 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2055 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2056 BS
            {31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            // Year 2057 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2058 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2059 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2060 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2061 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2062 BS
            {30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31},
            // Year 2063 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2064 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2065 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2066 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            // Year 2067 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2068 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2069 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2070 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2071 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2072 BS
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2073 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2074 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2075 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2076 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            // Year 2077 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            // Year 2078 BS
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2079 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2080 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            // Year 2081 BS
            {31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2082 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2083 BS
            {31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2084 BS
            {31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2085 BS
            {31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30},
            // Year 2086 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2087 BS
            {31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30},
            // Year 2088 BS
            {30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30},
            // Year 2089 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2090 BS
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            // Year 2091 BS (incomplete data - using approximate values)
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2092 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2093 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2094 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            // Year 2095 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            // Year 2096 BS
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            // Year 2097 BS
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            // Year 2098 BS
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            // Year 2099 BS
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}
    };

    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2099;

    // Reference date: 2000/01/01 BS = 1943/04/14 AD
    private static final LocalDate REFERENCE_AD_DATE = LocalDate.of(1943, 4, 14);
    private static final int REFERENCE_BS_YEAR = 2000;
    private static final int REFERENCE_BS_MONTH = 1;
    private static final int REFERENCE_BS_DAY = 1;

    /**
     * Constructor from string in "YYYY/MM/DD" or "YYYY-MM-DD" format
     */
    public NepaliDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }

        String normalized = dateString.trim();
        String[] parts;

        if (normalized.contains("/")) {
            parts = normalized.split("/");
        } else if (normalized.contains("-")) {
            parts = normalized.split("-");
        } else {
            throw new IllegalArgumentException("Invalid date format. Use YYYY/MM/DD or YYYY-MM-DD");
        }

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected YYYY/MM/DD or YYYY-MM-DD");
        }

        try {
            this.year = Integer.parseInt(parts[0]);
            this.month = Integer.parseInt(parts[1]);
            this.day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date components. Year, month, and day must be integers");
        }

        if (!isValidDate(year, month, day)) {
            throw new IllegalArgumentException("Invalid Nepali date: " + dateString);
        }
    }

    /**
     * Validate if the given Nepali date is valid
     */
    public static boolean isValidDate(int year, int month, int day) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1) {
            return false;
        }

        int daysInMonth = getDaysInMonth(year, month);
        return day <= daysInMonth;
    }

    /**
     * Get number of days in a specific month of a year
     */
    public static int getDaysInMonth(int year, int month) {
        if (year < MIN_YEAR || year > MAX_YEAR || month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid year or month");
        }
        return NEPALI_CALENDAR[year - MIN_YEAR][month - 1];
    }

    /**
     * Convert AD (Gregorian) LocalDate to Nepali Date
     */
    public static NepaliDate convertLocalDate(LocalDate adDate) {
        if (adDate == null) {
            throw new IllegalArgumentException("AD date cannot be null");
        }

        if (adDate.isBefore(REFERENCE_AD_DATE)) {
            throw new IllegalArgumentException("Date is before the supported range (before 2000/01/01 BS)");
        }

        long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(REFERENCE_AD_DATE, adDate);

        int bsYear = REFERENCE_BS_YEAR;
        int bsMonth = REFERENCE_BS_MONTH;
        int bsDay = REFERENCE_BS_DAY;

        bsDay += (int) daysDifference;

        // Adjust for days overflow
        while (bsDay > getDaysInMonth(bsYear, bsMonth)) {
            bsDay -= getDaysInMonth(bsYear, bsMonth);
            bsMonth++;

            if (bsMonth > 12) {
                bsMonth = 1;
                bsYear++;
            }

            if (bsYear > MAX_YEAR) {
                throw new IllegalArgumentException("Date is beyond supported range (after 2099/12/30 BS)");
            }
        }

        return new NepaliDate(bsYear, bsMonth, bsDay);
    }

    /**
     * Convert AD date string to Nepali Date
     */
    public static NepaliDate convertADString(String adDateString) {
        if (adDateString == null || adDateString.trim().isEmpty()) {
            throw new IllegalArgumentException("AD date string cannot be null or empty");
        }

        try {
            LocalDate adDate;
            if (adDateString.contains("/")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                adDate = LocalDate.parse(adDateString, formatter);
            } else if (adDateString.contains("-")) {
                adDate = LocalDate.parse(adDateString);
            } else {
                throw new IllegalArgumentException("Invalid AD date format. Use yyyy/MM/dd or yyyy-MM-dd");
            }

            return convertLocalDate(adDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid AD date string: " + adDateString, e);
        }
    }

    /**
     * Convert Nepali Date to AD (Gregorian) LocalDate
     */
    public static LocalDate convertToLocalDate(NepaliDate nepaliDate) {
        if (nepaliDate == null) {
            throw new IllegalArgumentException("Nepali date cannot be null");
        }

        if (!isValidDate(nepaliDate.year, nepaliDate.month, nepaliDate.day)) {
            throw new IllegalArgumentException("Invalid Nepali date");
        }

        // Calculate total days from reference BS date to given BS date
        int totalDays = 0;

        // Add days for complete years
        for (int year = REFERENCE_BS_YEAR; year < nepaliDate.year; year++) {
            totalDays += getTotalDaysInYear(year);
        }

        // Add days for complete months in the target year
        for (int month = 1; month < nepaliDate.month; month++) {
            totalDays += getDaysInMonth(nepaliDate.year, month);
        }

        // Add remaining days
        totalDays += nepaliDate.day - REFERENCE_BS_DAY;

        return REFERENCE_AD_DATE.plusDays(totalDays);
    }

    /**
     * Convert this Nepali date to AD LocalDate
     */
    public LocalDate toLocalDate() {
        return convertToLocalDate(this);
    }

    /**
     * Get total days in a Nepali year
     */
    public static int getTotalDaysInYear(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }

        int total = 0;
        for (int month = 1; month <= 12; month++) {
            total += getDaysInMonth(year, month);
        }
        return total;
    }

    /**
     * Get current Nepali date
     */
    public static NepaliDate now() {
        return convertLocalDate(LocalDate.now());
    }

    /**
     * Add days to this date
     */
    public NepaliDate plusDays(int days) {
        LocalDate adDate = this.toLocalDate().plusDays(days);
        return convertLocalDate(adDate);
    }

    /**
     * Add months to this date
     */
    public NepaliDate plusMonths(int months) {
        int newMonth = this.month + months;
        int newYear = this.year;

        while (newMonth > 12) {
            newMonth -= 12;
            newYear++;
        }

        while (newMonth < 1) {
            newMonth += 12;
            newYear--;
        }

        int daysInNewMonth = getDaysInMonth(newYear, newMonth);
        int newDay = Math.min(this.day, daysInNewMonth);

        return new NepaliDate(newYear, newMonth, newDay);
    }

    /**
     * Add years to this date
     */
    public NepaliDate plusYears(int years) {
        int newYear = this.year + years;
        int daysInMonth = getDaysInMonth(newYear, this.month);
        int newDay = Math.min(this.day, daysInMonth);

        return new NepaliDate(newYear, this.month, newDay);
    }

    /**
     * Format date as YYYY/MM/DD
     */
    public String format() {
        return String.format("%04d/%02d/%02d", year, month, day);
    }

    /**
     * Format date as YYYY-MM-DD
     */
    public String formatWithDash() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Get month name in English
     */
    public String getMonthName() {
        String[] months = {
                "Baisakh", "Jestha", "Ashadh", "Shrawan",
                "Bhadra", "Ashwin", "Kartik", "Mangsir",
                "Poush", "Magh", "Falgun", "Chaitra"
        };
        return months[month - 1];
    }

    /**
     * Get month name in Nepali
     */
    public String getMonthNameNepali() {
        String[] months = {
                "बैशाख", "जेष्ठ", "आषाढ", "श्रावण",
                "भाद्र", "आश्विन", "कार्तिक", "मंसिर",
                "पौष", "माघ", "फाल्गुन", "चैत्र"
        };
        return months[month - 1];
    }

    /**
     * Check if this date is before another date
     */
    public boolean isBefore(NepaliDate other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Check if this date is after another date
     */
    public boolean isAfter(NepaliDate other) {
        return this.compareTo(other) > 0;
    }

    @Override
    public int compareTo(NepaliDate other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NepaliDate that = (NepaliDate) obj;
        return year == that.year && month == that.month && day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return format();
    }
}
