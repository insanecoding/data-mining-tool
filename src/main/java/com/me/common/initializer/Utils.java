package com.me.common.initializer;

public final class Utils {
    private Utils(){}

    /**
     * convert deserialized val (which could be string or int) into strict int
     */
    public static int stringToInt(Object val) {
        String intStr = String.valueOf(val);
        return Integer.parseInt(intStr);
    }

    /**
     * convert deserialized val (which could be string or double) into strict double
     */
    public static double stringToDouble(Object val) {
        String doubleStr = String.valueOf(val);
        return Double.parseDouble(doubleStr);
    }
}
