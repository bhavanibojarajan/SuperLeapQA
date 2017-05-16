package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements NumericSelectableInputType {
    LB,
    KG;

    /**
     * @return A {@link String} value.
     * @see NumericSelectableInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public String emptyInputErrorFormat() {
        return "register_error_weightValueNotSet";
    }

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
            case LB:
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
            case LB:
                return "btn_lb";

            case KG:
                return "btn_kg";

            default:
                return "";
        }
    }

    /**
     * Convert a weight value to lb.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double lb(double value) {
        switch (this) {
            case LB:
                return value;

            case KG:
                return value * 2.20462d;

            default:
                return 0;
        }
    }

    /**
     * Convert a weight value to kg.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    public double kg(double value) {
        switch (this) {
            case KG:
                return value;

            case LB:
                return value * 0.453592d;

            default:
                return 0;
        }
    }

    /**
     * Get a weight value's {@link String} representation in kg.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     */
    @NotNull
    public String kgString(double value) {
        return String.format("%.1f kg", kg(value));
    }

    /**
     * Get a weight value's {@link String} representation in lbs.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     */
    @NotNull
    public String lbString(double value) {
        return String.format("%d lbs", (int)Math.round(lb(value)));
    }

    /**
     * Get the weight value by parsing a {@link String} representation of
     * a weight.
     * @param value A {@link String} value.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#numericValue(String)
     */
    @Override
    public double numericValue(@NotNull String value) {
        String regex;
        double ratio;

        switch (this) {
            case KG:
                regex = "(\\d+).(\\d+) kg";
                ratio = 10;
                break;

            case LB:
                regex = "(\\d+) lbs";
                ratio = 1;
                break;

            default:
                return 0;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        double base = matcher.find() ? Double.valueOf(matcher.group(1)) : 0;
        double dec = matcher.groupCount() > 1 ? Double.valueOf(matcher.group(2)) : 0;
        return base + dec / ratio;
    }

    /**
     * Get the appropriately formatted weight {@link String}, depending on
     * the type of {@link Weight}.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #kgString(double)
     * @see #lbString(double)
     * @see NumericSelectableInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        switch (this) {
            case KG:
                return kgString(value);

            case LB:
                return lbString(value);

            default:
                return "";
        }
    }

    /**
     * Get the minimum selectable weight.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#minSelectableNumericValue()
     */
    @Override
    public double minSelectableNumericValue() {
        switch (this) {
            case LB:
                return 26;

            case KG:
                return 10;

            default:
                return 0;
        }
    }

    /**
     * Get the maximum selectable weight. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#maxSelectableNumericValue()
     */
    @Override
    public double maxSelectableNumericValue() {
        switch (this) {
            case LB:
                return 485;

            case KG:
                return 220;

            default:
                return 0;
        }
    }

    /**
     * Get the selectable weight step.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#selectableNumericValueStep()
     */
    @Override
    public double selectableNumericValueStep() {
        switch (this) {
            case LB:
                return 1;

            case KG:
                return 0.5;

            default:
                return 0;
        }
    }
}
