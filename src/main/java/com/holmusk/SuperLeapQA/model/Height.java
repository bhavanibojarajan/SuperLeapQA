package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.NumericSelectableInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements NumericSelectableInputType {
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
                return value * 30.48d;

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
        return String.format("%.1f cm", cm(value));
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

            case CM:
                return value / 30.48d;

            default:
                return 0;
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
        int remain = (int)Math.round(((ft - base) * 12));

        /* Since we use ceil for the remainder value, if it rounds up to 12,
         * we need to add 1 to base and reset remain to 0 - i.e. a full foot
         * from 12 inches */
        if (remain % 12 == 0) {
            base += remain / 12;
            remain = 0;
        }

        return String.format("%1$d'%2$d\"", (int)base, remain);
    }

    /**
     * Get the appropriately formatted height {@link String}, depending on
     * the type of {@link Height}.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #ftString(double)
     * @see #cmString(double)
     * @see NumericSelectableInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        switch (this) {
            case FT:
                return ftString(value);

            case CM:
                return cmString(value);

            default:
                return "";
        }
    }

    /**
     * Get the height value by parsing a {@link String} representation of
     * a height.
     * @param value A {@link String} value.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#numericValue(String)
     */
    @Override
    public double numericValue(@NotNull String value) {
        String regex;
        double ratio;

        switch (this) {
            case FT:
                regex = "(\\d+)'(\\d+)\"";
                ratio = 12;
                break;

            case CM:
                regex = "(\\d+)\\.(\\d+) cm";
                ratio = 10;
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
     * Get the minimum selectable height.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#minSelectableNumericValue()
     */
    public double minSelectableNumericValue() {
        switch (this) {
            case CM:
                return 50;

            case FT:
                return CM.ft(CM.minSelectableNumericValue());

            default:
                return 0;
        }
    }

    /**
     * Get the maximum selectable height. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#maxSelectableNumericValue()
     */
    @Override
    public double maxSelectableNumericValue() {
        switch (this) {
            case CM:
//                return 250;
                return 100;

            case FT:
                return CM.ft(CM.maxSelectableNumericValue());

            default:
                return 0;
        }
    }

    /**
     * Get the step value that is the difference between a value and its
     * immediately higher value.
     * @return A {@link Double} value.
     * @see NumericSelectableInputType#selectableNumericValueStep()
     */
    @Override
    public double selectableNumericValueStep() {
        switch (this) {
            case CM:
                return 0.5d;

            case FT:
                return CM.ft(CM.selectableNumericValueStep());

            default:
                return 0;
        }
    }
}
