package com.progress.coolProject.Utils.date;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.StringConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NepaliDate implements Comparable<NepaliDate> {
    private int year;
    private int month;
    private int day;


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
        if (!NepaliDateLookup.hasDataForYear(year)) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1) {
            return false;
        }

        try {
            int daysInMonth = NepaliDateLookup.getDaysInMonth(year, month);
            return day <= daysInMonth;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get number of days in a specific month of a year
     */
    public static int getDaysInMonth(int year, int month) {
        return NepaliDateLookup.getDaysInMonth(year, month);
    }

    /**
     * Convert AD (Gregorian) LocalDate to Nepali Date
     * Uses optimized HashMap lookup for O(1) access
     */
    public static NepaliDate convertLocalDate(LocalDate adDate) {
        if (adDate == null) {
            throw new IllegalArgumentException("AD date cannot be null");
        }

        // Find the closest Baisakh 1 (New Year) that is <= adDate
        int closestBsYear = -1;
        LocalDate closestBaisakhFirst = null;

        // Binary search would be more efficient, but for now iterate
        for (int bsYear = NepaliDateLookup.MIN_YEAR; bsYear <= NepaliDateLookup.MAX_YEAR; bsYear++) {
            try {
                LocalDate baisakhFirst = NepaliDateLookup.getBaisakhFirstAD(bsYear);
                if (!baisakhFirst.isAfter(adDate)) {
                    closestBsYear = bsYear;
                    closestBaisakhFirst = baisakhFirst;
                } else {
                    break; // Found the year, no need to continue
                }
            } catch (IllegalArgumentException e) {
                // No data for this year, continue
                log.debug("No data for year: " + bsYear);
                continue;
            }
        }

        if (closestBsYear == -1) {
            throw new IllegalArgumentException("Date is before the supported range");
        }

        // Calculate days difference from Baisakh 1
        long daysDifference = ChronoUnit.DAYS.between(closestBaisakhFirst, adDate);

        // Now add days to Baisakh 1 of closestBsYear
        int bsYear = closestBsYear;
        int bsMonth = 1; // Baisakh
        int bsDay = 1;

        while (daysDifference > 0) {
            int daysInCurrentMonth = NepaliDateLookup.getDaysInMonth(bsYear, bsMonth);
            int daysRemainingInMonth = daysInCurrentMonth - bsDay + 1;

            if (daysDifference < daysRemainingInMonth) {
                bsDay += (int) daysDifference;
                daysDifference = 0;
            } else {
                daysDifference -= daysRemainingInMonth;
                bsDay = 1;
                bsMonth++;

                if (bsMonth > 12) {
                    bsMonth = 1;
                    bsYear++;

                    if (!NepaliDateLookup.hasDataForYear(bsYear)) {
                        throw new IllegalArgumentException("Date is beyond supported range");
                    }
                }
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
     * Uses optimized HashMap lookup
     */
    public static LocalDate convertToLocalDate(NepaliDate nepaliDate) {
        if (nepaliDate == null) {
            throw new IllegalArgumentException("Nepali date cannot be null");
        }

        if (!isValidDate(nepaliDate.year, nepaliDate.month, nepaliDate.day)) {
            throw new IllegalArgumentException("Invalid Nepali date");
        }

        // Get the Baisakh 1 AD equivalent for this year
        LocalDate baisakhFirst = NepaliDateLookup.getBaisakhFirstAD(nepaliDate.year);

        // Calculate days to add from Baisakh 1
        int daysToAdd = 0;

        // Add days from complete months
        for (int month = 1; month < nepaliDate.month; month++) {
            daysToAdd += NepaliDateLookup.getDaysInMonth(nepaliDate.year, month);
        }

        // Add remaining days in current month
        daysToAdd += nepaliDate.day - 1; // -1 because Baisakh 1 is day 1

        return baisakhFirst.plusDays(daysToAdd);
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
        return NepaliDateLookup.getTotalDaysInYear(year);
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
        return StringConstants.months[month - 1];
    }

    /**
     * Get month name in Nepali
     */
    public String getMonthNameNepali() {
        return StringConstants.nepaliMonths[month - 1];
    }

    public String getYearInNepali(){
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setGroupingUsed(false);
        nepaliFormat.setMinimumFractionDigits(0);
        return nepaliFormat.format(year);
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
