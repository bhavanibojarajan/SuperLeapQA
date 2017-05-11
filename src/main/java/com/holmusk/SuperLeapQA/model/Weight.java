package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.List;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements ValueRangeConverterType<Double> {
    LBS,
    KG;

    @NotNull
    @Override
    public Converter<Double> converter() {
        return a -> a;
    }

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

    /**
     * Get the minimum selectable weight.
     * @return A {@link Double} value.
     */
    public double minSelectableWeight() {
        switch (this) {
            case LBS:
                return 0;

            case KG:
                return 10;

            default:
                return 0;
        }
    }

    /**
     * Get the maximum selectable weight.
     * @return A {@link Double} value.
     */
    public double maxSelectableWeight() {
        switch (this) {
            case LBS:
                return 0;

            case KG:
                return 219;

            default:
                return 0;
        }
    }

    /**
     * Get the selectable weight step.
     * @return A {@link Double} value.
     */
    public double selectableWeightStep() {
        switch (this) {
            case LBS:
                return 0;

            case KG:
                return 1;

            default:
                return 0;
        }
    }

    /**
     * Get the selectable weight range.
     * @return A {@link List} of {@link Double}.
     * @see #valueRange(Number, Number, Number)
     * @see #minSelectableWeight()
     * @see #maxSelectableWeight()
     * @see #selectableWeightStep()
     */
    @NotNull
    public List<Double> selectableWeightRange() {
        return valueRange(
            minSelectableWeight(),
            maxSelectableWeight(),
            selectableWeightStep()
        );
    }
}
