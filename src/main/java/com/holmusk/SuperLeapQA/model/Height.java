package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height {
    FT,
    CM;

    /**
     * Get the localizable title for the current {@link Height}.
     * @return A {@link String} value.
     */
    @NotNull
    public String localizable() {
        switch (this) {
            case FT:
                return "user_title_height_ft";

            case CM:
                return "user_title_height_cm";

            default:
                return "";
        }
    }

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case FT:
                return "btn_ft";

            case CM:
                return "btn_cm";

            default:
                return "";
        }
    }

    /**
     * Convert a height value to cm.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double cm(double value) {
        switch (this) {
            case FT:
                return value / 0.0328084d;

            default:
                return value;
        }
    }

    /**
     * Get a height value's {@link String} representation in cm.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #cm(double)
     */
    @NotNull
    public String cmString(double value) {
        return String.format("%d cm", (int)cm(value));
    }

    /**
     * Convert a height value to ft.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double ft(double value) {
        switch (this) {
            case FT:
                return value;

            default:
                return value * 0.0328084d;
        }
    }

    /**
     * Get a height value's {@link String} representation in ft.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #ft(double)
     */
    @NotNull
    public String ftString(double value) {
        double ft = ft(value);
        double base = Math.floor(ft);
        double remain = ft - base;
        return String.format("%1$d'%2$.2f\"", (int)base, remain);
    }
}
