package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum  Weight {
    LBS,
    KG;

    /**
     * Get the localizable title for the current {@link Weight}.
     * @return A {@link String} value.
     */
    @NotNull
    public String localizable() {
        switch (this) {
            case LBS:
                return "user_title_weight_lbs";

            case KG:
                return "user_title_weight_kg";

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
            case LBS:
                return "btn_lb";

            case KG:
                return "btn_kg";

            default:
                return "";
        }
    }

    /**
     * Convert a weight value to kg.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double kg(double value) {
        switch (this) {
            case LBS:
                return value / 0.453592d;

            default:
                return value;
        }
    }

    /**
     * Get a weight value's {@link String} representation in kg.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #kg(double)
     */
    @NotNull
    public String kgString(double value) {
        return String.format("%d kg", (int)kg(value));
    }

    /**
     * Convert a weight value to lbs.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double lbs(double value) {
        switch (this) {
            case KG:
                return value * 0.453592d;

            default:
                return value;
        }
    }

    /**
     * Get a weight value's {@link String} representation in lbs.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #lbs(double)
     */
    @NotNull
    public String lbsString(double value) {
        return String.format("%.2f", lbs(value));
    }
}
