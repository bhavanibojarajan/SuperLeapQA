package com.holmusk.SuperLeapQA.model;

import org.apache.xerces.impl.xpath.regex.Match;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements ValueRangeConverterType<Double> {
    FT,
    CM;

    @NotNull
    @Override
    public Converter<Double> converter() {
        return a -> Double.valueOf(String.format("%.2f", a));
    }

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
        double remain = (ft - base) * 12;
        return String.format("%1$d'%2$d\"", (int)base, (int)Math.ceil(remain));
    }

    /**
     * Get the appropriately formatted height {@link String}, depending on
     * the type of {@link Height}.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see #ftString(double)
     * @see #cmString(double)
     */
    @NotNull
    public String heightString(double value) {
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
     * Get the height value by parsing a {@link String} representatino of
     * a height.
     * @param value A {@link String} value.
     * @return A {@link Double} value.
     */
    public double heightValue(@NotNull String value) {
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

        if (matcher.find()) {
            double base = Double.valueOf(matcher.group(1));
            double decimal = Double.valueOf(matcher.group(2));
            return base + decimal / ratio;
        } else {
            return 0;
        }
    }

    /**
     * Get the minimum selectable height.
     * @return A {@link Double} value.
     */
    public double minSelectableHeight() {
        switch (this) {
            case CM:
                return 50;

            case FT:
                return CM.ft(CM.minSelectableHeight());

            default:
                return 0;
        }
    }

    /**
     * Get the maximum selectable height.
     * @return A {@link Double} value.
     */
    public double maxSelectableHeight() {
        switch (this) {
            case CM:
                return 250;

            case FT:
                return CM.ft(CM.maxSelectableHeight());

            default:
                return 0;
        }
    }

    /**
     * Get the step value that is the difference between a value and its
     * immediately higher value.
     * @return A {@link Double} value.
     */
    public double selectableHeightStep() {
        switch (this) {
            case CM:
                return 0.5d;

            case FT:
                return CM.ft(CM.selectableHeightStep());

            default:
                return 0;
        }
    }

    /**
     * Get the selectable height range.
     * @return A {@link List} of {@link Double}.
     * @see #valueRange(Number, Number, Number)
     * @see #minSelectableHeight()
     * @see #maxSelectableHeight()
     * @see #selectableHeightStep()
     */
    @NotNull
    public List<Double> selectableHeightRange() {
        return valueRange(
            minSelectableHeight(),
            maxSelectableHeight(),
            selectableHeightStep()
        );
    }
}
