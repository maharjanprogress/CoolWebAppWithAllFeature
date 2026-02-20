package com.progress.coolProject.Utils.date;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Lookup table for Nepali calendar data
 * Maps Bikram Sambat years to:
 * 1. Number of days in each month
 * 2. Corresponding AD date for Baisakh 1 (New Year)
 */
public class NepaliDateLookup {

    /**
     * Bikram Sambat new year (Baisakh 1) equivalent Gregorian dates
     * Key: BS Year, Value: LocalDate (AD equivalent of Baisakh 1)
     */
    static final Map<Integer, LocalDate> baisakhFirstADEquivalent = new HashMap<>();

    /**
     * Number of days in each Nepali month for each year
     * Key: BS Year, Value: Array of 12 integers (days in each month)
     */
    static final Map<Integer, int[]> daysInMonths = new HashMap<>();

    static final int MIN_YEAR = 1970;
    static final int MAX_YEAR = 2100;

    static {
        // Initialize Baisakh 1 AD equivalents
        initializeBaisakhFirstDates();

        // Initialize days in months
        initializeDaysInMonths();
    }

    private static void initializeBaisakhFirstDates() {
        daysInMonths.put(1970, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1971, new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1972, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(1973, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(1974, new int[]{31, 31, 32, 30, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1975, new int[]{31, 31, 32, 32, 30, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1976, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(1977, new int[]{30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31});
        daysInMonths.put(1978, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1979, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1980, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(1981, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(1982, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1983, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1984, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(1985, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(1986, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1987, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1988, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(1989, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(1990, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1991, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(1992, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(1993, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1994, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1995, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(1996, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(1997, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1998, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(1999, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2000, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2001, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2002, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2003, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2004, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 30, 29, 29, 31});
        daysInMonths.put(2005, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2006, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2007, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2008, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        daysInMonths.put(2009, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2010, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2011, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2012, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(2013, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2014, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2015, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2016, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(2017, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2018, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2019, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2020, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2021, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2022, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(2023, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2024, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2025, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2026, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2027, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2028, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2029, new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2030, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2031, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2032, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2033, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2034, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2035, new int[]{30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        daysInMonths.put(2036, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2037, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2038, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2039, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(2040, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2041, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2042, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2043, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(2044, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2045, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2046, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2047, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2048, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2049, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(2050, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2051, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2052, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2053, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(2054, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2055, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2056, new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2057, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2058, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2059, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2060, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2061, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2062, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31});
        daysInMonths.put(2063, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2064, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2065, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2066, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        daysInMonths.put(2067, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2068, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2069, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2070, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        daysInMonths.put(2071, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2072, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2073, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        daysInMonths.put(2074, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2075, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2076, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(2077, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2078, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2079, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2080, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        daysInMonths.put(2081, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        daysInMonths.put(2082, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2083, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2084, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2085, new int[]{31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2086, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2087, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 30, 30, 30});
        daysInMonths.put(2088, new int[]{30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2089, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2090, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2091, new int[]{31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2092, new int[]{30, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2093, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2094, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2095, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 30, 30, 30});
        daysInMonths.put(2096, new int[]{30, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        daysInMonths.put(2097, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        daysInMonths.put(2098, new int[]{31, 31, 32, 31, 31, 31, 29, 30, 29, 30, 29, 31});
        daysInMonths.put(2099, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 29, 30, 30, 30});
        daysInMonths.put(2100, new int[]{31, 32, 31, 32, 30, 31, 30, 29, 30, 29, 30, 30});
    }

    private static void initializeDaysInMonths() {
        baisakhFirstADEquivalent.put(1970, LocalDate.of(1913, 4, 13));
        baisakhFirstADEquivalent.put(1971, LocalDate.of(1914, 4, 13));
        baisakhFirstADEquivalent.put(1972, LocalDate.of(1915, 4, 13));
        baisakhFirstADEquivalent.put(1973, LocalDate.of(1916, 4, 13));
        baisakhFirstADEquivalent.put(1974, LocalDate.of(1917, 4, 13));
        baisakhFirstADEquivalent.put(1975, LocalDate.of(1918, 4, 12));
        baisakhFirstADEquivalent.put(1976, LocalDate.of(1919, 4, 13));
        baisakhFirstADEquivalent.put(1977, LocalDate.of(1920, 4, 13));
        baisakhFirstADEquivalent.put(1978, LocalDate.of(1921, 4, 13));
        baisakhFirstADEquivalent.put(1979, LocalDate.of(1922, 4, 13));
        baisakhFirstADEquivalent.put(1980, LocalDate.of(1923, 4, 13));
        baisakhFirstADEquivalent.put(1981, LocalDate.of(1924, 4, 13));
        baisakhFirstADEquivalent.put(1982, LocalDate.of(1925, 4, 13));
        baisakhFirstADEquivalent.put(1983, LocalDate.of(1926, 4, 13));
        baisakhFirstADEquivalent.put(1984, LocalDate.of(1927, 4, 13));
        baisakhFirstADEquivalent.put(1985, LocalDate.of(1928, 4, 13));
        baisakhFirstADEquivalent.put(1986, LocalDate.of(1929, 4, 13));
        baisakhFirstADEquivalent.put(1987, LocalDate.of(1930, 4, 13));
        baisakhFirstADEquivalent.put(1988, LocalDate.of(1931, 4, 13));
        baisakhFirstADEquivalent.put(1989, LocalDate.of(1932, 4, 13));
        baisakhFirstADEquivalent.put(1990, LocalDate.of(1933, 4, 13));
        baisakhFirstADEquivalent.put(1991, LocalDate.of(1934, 4, 13));
        baisakhFirstADEquivalent.put(1992, LocalDate.of(1935, 4, 13));
        baisakhFirstADEquivalent.put(1993, LocalDate.of(1936, 4, 13));
        baisakhFirstADEquivalent.put(1994, LocalDate.of(1937, 4, 13));
        baisakhFirstADEquivalent.put(1995, LocalDate.of(1938, 4, 13));
        baisakhFirstADEquivalent.put(1996, LocalDate.of(1939, 4, 13));
        baisakhFirstADEquivalent.put(1997, LocalDate.of(1940, 4, 13));
        baisakhFirstADEquivalent.put(1998, LocalDate.of(1941, 4, 13));
        baisakhFirstADEquivalent.put(1999, LocalDate.of(1942, 4, 13));
        baisakhFirstADEquivalent.put(2000, LocalDate.of(1943, 4, 14));
        baisakhFirstADEquivalent.put(2001, LocalDate.of(1944, 4, 13));
        baisakhFirstADEquivalent.put(2002, LocalDate.of(1945, 4, 13));
        baisakhFirstADEquivalent.put(2003, LocalDate.of(1946, 4, 13));
        baisakhFirstADEquivalent.put(2004, LocalDate.of(1947, 4, 14));
        baisakhFirstADEquivalent.put(2005, LocalDate.of(1948, 4, 13));
        baisakhFirstADEquivalent.put(2006, LocalDate.of(1949, 4, 13));
        baisakhFirstADEquivalent.put(2007, LocalDate.of(1950, 4, 13));
        baisakhFirstADEquivalent.put(2008, LocalDate.of(1951, 4, 14));
        baisakhFirstADEquivalent.put(2009, LocalDate.of(1952, 4, 13));
        baisakhFirstADEquivalent.put(2010, LocalDate.of(1953, 4, 13));
        baisakhFirstADEquivalent.put(2011, LocalDate.of(1954, 4, 13));
        baisakhFirstADEquivalent.put(2012, LocalDate.of(1955, 4, 14));
        baisakhFirstADEquivalent.put(2013, LocalDate.of(1956, 4, 13));
        baisakhFirstADEquivalent.put(2014, LocalDate.of(1957, 4, 13));
        baisakhFirstADEquivalent.put(2015, LocalDate.of(1958, 4, 13));
        baisakhFirstADEquivalent.put(2016, LocalDate.of(1959, 4, 14));
        baisakhFirstADEquivalent.put(2017, LocalDate.of(1960, 4, 13));
        baisakhFirstADEquivalent.put(2018, LocalDate.of(1961, 4, 13));
        baisakhFirstADEquivalent.put(2019, LocalDate.of(1962, 4, 13));
        baisakhFirstADEquivalent.put(2020, LocalDate.of(1963, 4, 14));
        baisakhFirstADEquivalent.put(2021, LocalDate.of(1964, 4, 13));
        baisakhFirstADEquivalent.put(2022, LocalDate.of(1965, 4, 13));
        baisakhFirstADEquivalent.put(2023, LocalDate.of(1966, 4, 13));
        baisakhFirstADEquivalent.put(2024, LocalDate.of(1967, 4, 14));
        baisakhFirstADEquivalent.put(2025, LocalDate.of(1968, 4, 13));
        baisakhFirstADEquivalent.put(2026, LocalDate.of(1969, 4, 13));
        baisakhFirstADEquivalent.put(2027, LocalDate.of(1970, 4, 14));
        baisakhFirstADEquivalent.put(2028, LocalDate.of(1971, 4, 14));
        baisakhFirstADEquivalent.put(2029, LocalDate.of(1972, 4, 13));
        baisakhFirstADEquivalent.put(2030, LocalDate.of(1973, 4, 13));
        baisakhFirstADEquivalent.put(2031, LocalDate.of(1974, 4, 14));
        baisakhFirstADEquivalent.put(2032, LocalDate.of(1975, 4, 14));
        baisakhFirstADEquivalent.put(2033, LocalDate.of(1976, 4, 13));
        baisakhFirstADEquivalent.put(2034, LocalDate.of(1977, 4, 13));
        baisakhFirstADEquivalent.put(2035, LocalDate.of(1978, 4, 14));
        baisakhFirstADEquivalent.put(2036, LocalDate.of(1979, 4, 14));
        baisakhFirstADEquivalent.put(2037, LocalDate.of(1980, 4, 13));
        baisakhFirstADEquivalent.put(2038, LocalDate.of(1981, 4, 13));
        baisakhFirstADEquivalent.put(2039, LocalDate.of(1982, 4, 14));
        baisakhFirstADEquivalent.put(2040, LocalDate.of(1983, 4, 14));
        baisakhFirstADEquivalent.put(2041, LocalDate.of(1984, 4, 13));
        baisakhFirstADEquivalent.put(2042, LocalDate.of(1985, 4, 13));
        baisakhFirstADEquivalent.put(2043, LocalDate.of(1986, 4, 14));
        baisakhFirstADEquivalent.put(2044, LocalDate.of(1987, 4, 14));
        baisakhFirstADEquivalent.put(2045, LocalDate.of(1988, 4, 13));
        baisakhFirstADEquivalent.put(2046, LocalDate.of(1989, 4, 13));
        baisakhFirstADEquivalent.put(2047, LocalDate.of(1990, 4, 14));
        baisakhFirstADEquivalent.put(2048, LocalDate.of(1991, 4, 14));
        baisakhFirstADEquivalent.put(2049, LocalDate.of(1992, 4, 13));
        baisakhFirstADEquivalent.put(2050, LocalDate.of(1993, 4, 13));
        baisakhFirstADEquivalent.put(2051, LocalDate.of(1994, 4, 14));
        baisakhFirstADEquivalent.put(2052, LocalDate.of(1995, 4, 14));
        baisakhFirstADEquivalent.put(2053, LocalDate.of(1996, 4, 13));
        baisakhFirstADEquivalent.put(2054, LocalDate.of(1997, 4, 13));
        baisakhFirstADEquivalent.put(2055, LocalDate.of(1998, 4, 14));
        baisakhFirstADEquivalent.put(2056, LocalDate.of(1999, 4, 14));
        baisakhFirstADEquivalent.put(2057, LocalDate.of(2000, 4, 13));
        baisakhFirstADEquivalent.put(2058, LocalDate.of(2001, 4, 14));
        baisakhFirstADEquivalent.put(2059, LocalDate.of(2002, 4, 14));
        baisakhFirstADEquivalent.put(2060, LocalDate.of(2003, 4, 14));
        baisakhFirstADEquivalent.put(2061, LocalDate.of(2004, 4, 13));
        baisakhFirstADEquivalent.put(2062, LocalDate.of(2005, 4, 14));
        baisakhFirstADEquivalent.put(2063, LocalDate.of(2006, 4, 14));
        baisakhFirstADEquivalent.put(2064, LocalDate.of(2007, 4, 14));
        baisakhFirstADEquivalent.put(2065, LocalDate.of(2008, 4, 13));
        baisakhFirstADEquivalent.put(2066, LocalDate.of(2009, 4, 14));
        baisakhFirstADEquivalent.put(2067, LocalDate.of(2010, 4, 14));
        baisakhFirstADEquivalent.put(2068, LocalDate.of(2011, 4, 14));
        baisakhFirstADEquivalent.put(2069, LocalDate.of(2012, 4, 13));
        baisakhFirstADEquivalent.put(2070, LocalDate.of(2013, 4, 14));
        baisakhFirstADEquivalent.put(2071, LocalDate.of(2014, 4, 14));
        baisakhFirstADEquivalent.put(2072, LocalDate.of(2015, 4, 14));
        baisakhFirstADEquivalent.put(2073, LocalDate.of(2016, 4, 13));
        baisakhFirstADEquivalent.put(2074, LocalDate.of(2017, 4, 14));
        baisakhFirstADEquivalent.put(2075, LocalDate.of(2018, 4, 14));
        baisakhFirstADEquivalent.put(2076, LocalDate.of(2019, 4, 14));
        baisakhFirstADEquivalent.put(2077, LocalDate.of(2020, 4, 13));
        baisakhFirstADEquivalent.put(2078, LocalDate.of(2021, 4, 14));
        baisakhFirstADEquivalent.put(2079, LocalDate.of(2022, 4, 14));
        baisakhFirstADEquivalent.put(2080, LocalDate.of(2023, 4, 14));
        baisakhFirstADEquivalent.put(2081, LocalDate.of(2024, 4, 13));
        baisakhFirstADEquivalent.put(2082, LocalDate.of(2025, 4, 14));
        baisakhFirstADEquivalent.put(2083, LocalDate.of(2026, 4, 14));
        baisakhFirstADEquivalent.put(2084, LocalDate.of(2027, 4, 14));
        baisakhFirstADEquivalent.put(2085, LocalDate.of(2028, 4, 13));
        baisakhFirstADEquivalent.put(2086, LocalDate.of(2029, 4, 14));
        baisakhFirstADEquivalent.put(2087, LocalDate.of(2030, 4, 14));
        baisakhFirstADEquivalent.put(2088, LocalDate.of(2031, 4, 15));
        baisakhFirstADEquivalent.put(2089, LocalDate.of(2032, 4, 15));
        baisakhFirstADEquivalent.put(2090, LocalDate.of(2033, 4, 14));
        baisakhFirstADEquivalent.put(2091, LocalDate.of(2034, 4, 14));
        baisakhFirstADEquivalent.put(2092, LocalDate.of(2035, 4, 13));
        baisakhFirstADEquivalent.put(2093, LocalDate.of(2036, 4, 14));
        baisakhFirstADEquivalent.put(2094, LocalDate.of(2037, 4, 14));
        baisakhFirstADEquivalent.put(2095, LocalDate.of(2038, 4, 14));
        baisakhFirstADEquivalent.put(2096, LocalDate.of(2039, 4, 15));
        baisakhFirstADEquivalent.put(2097, LocalDate.of(2040, 4, 13));
        baisakhFirstADEquivalent.put(2098, LocalDate.of(2041, 4, 14));
        baisakhFirstADEquivalent.put(2099, LocalDate.of(2042, 4, 14));
        baisakhFirstADEquivalent.put(2100, LocalDate.of(2043, 4, 14));
    }

    /**
     * Get number of days in a specific month
     */
    public static int getDaysInMonth(int year, int month) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        int[] days = daysInMonths.get(year);
        if (days == null) {
            throw new IllegalArgumentException("No data available for year: " + year);
        }

        return days[month - 1];
    }

    /**
     * Get AD date for Baisakh 1 of given BS year
     */
    public static LocalDate getBaisakhFirstAD(int bsYear) {
        LocalDate date = baisakhFirstADEquivalent.get(bsYear);
        if (date == null) {
            throw new IllegalArgumentException("No data available for year: " + bsYear);
        }
        return date;
    }

    /**
     * Get total days in a Nepali year
     */
    public static int getTotalDaysInYear(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }

        int[] days = daysInMonths.get(year);
        if (days == null) {
            throw new IllegalArgumentException("No data available for year: " + year);
        }

        int total = 0;
        for (int day : days) {
            total += day;
        }
        return total;
    }

    /**
     * Check if year data is available
     */
    public static boolean hasDataForYear(int year) {
        return daysInMonths.containsKey(year) && baisakhFirstADEquivalent.containsKey(year);
    }

    private NepaliDateLookup() {
        // Utility class, prevent instantiation
    }
}
