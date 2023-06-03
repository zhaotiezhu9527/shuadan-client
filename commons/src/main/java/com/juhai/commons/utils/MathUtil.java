package com.juhai.commons.utils;

import java.math.BigDecimal;

public class MathUtil {

    public static BigDecimal scale(int newScale, BigDecimal number) {
        return number.setScale(newScale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal scale(BigDecimal number) {
        return scale(2, number);
    }

    public static BigDecimal scale(int newScale, double number) {
        return new BigDecimal(number).setScale(newScale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal scale(double number) {
        return scale(2, number);
    }
}
