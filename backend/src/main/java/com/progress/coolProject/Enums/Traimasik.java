package com.progress.coolProject.Enums;

import com.progress.coolProject.StringConstants;
import com.progress.coolProject.Utils.date.NepaliDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Traimasik {
    PAHILO("पहिलो", new int[]{4, 5, 6}),      // Shrawan (4), Bhadra (5), Ashoj (6)
    DOSHRO("दोश्रो", new int[]{7, 8, 9}),      // Kartik (7), Mangsir (8), Paush (9)
    TYESRO("तेश्रो", new int[]{10, 11, 12}),   // Magh (10), Falgun (11), Chaitra (12)
    CHAUTHO("चौथो", new int[]{1, 2, 3});     // Baisakh (1), Jestha (2), Ashadh (3)

    private final String locale;
    private final int[] months; // Nepali months (1-12)

    /**
     * Get the current quarter based on today's date converted to Bikram Sambat
     */
    public static Traimasik getCurrent() {
        NepaliDate nepaliDate = NepaliDate.now();
        int currentMonth = nepaliDate.getMonth();

        // Determine which quarter we're in
        return getTraimasikForMonth(currentMonth);

    }

    /**
     * Get all quarters up to and including this one
     */
    public Traimasik[] getIncludingPrevious() {
        int currentOrdinal = this.ordinal();
        Traimasik[] result = new Traimasik[currentOrdinal + 1];
        for (int i = 0; i <= currentOrdinal; i++) {
            result[i] = Traimasik.values()[i];
        }
        return result;
    }

    public static Traimasik getTraimasikForMonth(int exactMonth){
        for (Traimasik quarter : values()) {
            for (int month : quarter.months) {
                if (month == exactMonth) {
                    return quarter;
                }
            }
        }

        // Default to first quarter if something goes wrong
        return PAHILO;
    }

    public static String getmonthBetweenTraimasikofPlusmonth(int plusMonth){
        NepaliDate nepaliDate = NepaliDate.now().plusMonths(plusMonth);
        int previousMonth = nepaliDate.getMonth();

        Traimasik previousQuarter = getTraimasikForMonth(previousMonth);

        if (previousMonth == previousQuarter.getMonths()[0]){
            return StringConstants.nepaliMonths[previousQuarter.getMonths()[0]-1];
        }

        return StringConstants.nepaliMonths[previousQuarter.getMonths()[0]-1] + " देखि " + nepaliDate.getMonthNameNepali();
    }

    public static String getmonthBetweenTraimasikofPlusmonthConcatenated(int plusMonth){
        NepaliDate nepaliDate = NepaliDate.now().plusMonths(plusMonth);
        int previousMonth = nepaliDate.getMonth();

        Traimasik previousQuarter = getTraimasikForMonth(previousMonth);


        List<Integer> validMonths = new ArrayList<>();

        for (int month : previousQuarter.getMonths()) {
            if (month <= previousMonth) {
                validMonths.add(month);
            }
        }

        int[] monthsArray = validMonths.stream().mapToInt(i -> i).toArray();

        return StringConstants.getMonthsConcatenated(monthsArray);
    }
}
