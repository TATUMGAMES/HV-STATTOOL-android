package com.tatumgames.stattool.helper;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Tatum on 7/24/2015.
 */
public class InputFilterMinMax implements InputFilter {

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Method is used to check if a value is between a min, max range
     * @param a min
     * @param b max
     * @param c value
     * @return
     */
    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
