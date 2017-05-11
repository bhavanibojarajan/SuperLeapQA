package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Get a weight value's {@link String} representation in kg.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     */
    @NotNull
    public String kgString(double value) {
        return String.format("%.1f kg", value);
    }

    /**
     * Get a weight value's {@link String} representation in lbs.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     */
    @NotNull
    public String lbsString(double value) {
        return String.format("%d lbs", (int)value);
    }

    /**
     * Get the weight value by parsing a {@link String} representation of
     * a weight.
     * @param value A {@link String} value.
     * @return A {@link Double} value.
     */
    public double weightValue(@NotNull String value) {
        String regex;
        double ratio;

        switch (this) {
            case KG:
                regex = "(\\d+).(\\d+) kg";
                ratio = 10;
                break;

            case LBS:
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
     * Get the minimum selectable weight.
     * @return A {@link Double} value.
     */
    public double minSelectableWeight() {
        switch (this) {
            case LBS:
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
     */
    public double maxSelectableWeight() {
        switch (this) {
            case LBS:
//                return 485;
                return 200;

            case KG:
//                return 220;
                return 100;

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
                return 1;

            case KG:
                return 0.5;

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
