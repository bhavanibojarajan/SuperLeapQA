package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Pair;
import org.swiften.xtestkit.base.element.action.input.type.NumericInputType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements BaseErrorType, SLNumericInputType {
    CHILD_FT,
    CHILD_INCH,
    CHILD_CM,
    CHILD_CM_DEC,
    TEEN_FT,
    TEEN_INCH,
    TEEN_CM,
    TEEN_CM_DEC;

    /**
     * Get the {@link Height} instances for metric unit of measurement.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link SLNumericInputType}.
     */
    @NotNull
    public static List<SLNumericInputType> metric(@NotNull UserMode mode) {
        switch (mode) {
            case PARENT:
                return Arrays.asList(CHILD_CM, CHILD_CM_DEC);

            case TEEN_ABOVE_18:
            case TEEN_UNDER_18:
                return Arrays.asList(TEEN_CM, TEEN_CM_DEC);

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the {@link Height} instances for imperial unit of measurement.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link SLNumericInputType}.
     */
    @NotNull
    public static List<SLNumericInputType> imperial(@NotNull UserMode mode) {
        switch (mode) {
            case PARENT:
                return Arrays.asList(CHILD_FT, CHILD_INCH);

            case TEEN_ABOVE_18:
            case TEEN_UNDER_18:
                return Arrays.asList(TEEN_FT, TEEN_INCH);

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get a {@link List} of random metric input values.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Pair}.
     * @see #metric(UserMode)
     * @see NumericInputType#randomNumericValue()
     */
    @NotNull
    public static List<Pair<SLNumericInputType,Double>> randomMetric(@NotNull UserMode mode) {
        return metric(mode).stream()
            .map(a -> new Pair<>(a, a.randomNumericValue()))
            .collect(Collectors.toList());
    }

    /**
     * Get a {@link List} of random imperial input values.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Pair}.
     * @see #imperial(UserMode)
     * @see NumericInputType#randomNumericValue()
     */
    @NotNull
    public static List<Pair<SLNumericInputType,Double>> randomImperial(@NotNull UserMode mode) {
        return imperial(mode).stream()
            .map(a -> new Pair<>(a, a.randomNumericValue()))
            .collect(Collectors.toList());
    }

    /**
     * @return A {@link String} value.
     * @see SLNumericInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public String emptyInputErrorFormat() {
        return "register_error_heightValueNotSet";
    }

    /**
     * @return A {@link String} value.
     * @see AndroidInputType#androidViewId()
     */
    @NotNull
    @Override
    public String androidViewId() {
        switch (this) {
            case CHILD_FT:
            case CHILD_INCH:
            case TEEN_FT:
            case TEEN_INCH:
                return "btn_ft";

            case CHILD_CM:
            case CHILD_CM_DEC:
            case TEEN_CM:
            case TEEN_CM_DEC:
                return "btn_cm";

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the appropriately formatted height {@link String}, depending on
     * the type of {@link Height}.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     * @see NumericInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        return String.valueOf((int)value);
    }

    /**
     * Get the minimum selectable height.
     * @return A {@link Double} value.
     * @see NumericInputType#minSelectableNumericValue()
     */
    public double minSelectableNumericValue() {
        switch (this) {
            case CHILD_CM:
                return 50;

            case CHILD_FT:
                return 1;

            case TEEN_CM:
                return 120;

            case TEEN_FT:
                return 3;

            case TEEN_CM_DEC:
            case CHILD_CM_DEC:
                return 0;

            case TEEN_INCH:
            case CHILD_INCH:
                return 0;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the maximum selectable height. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @return A {@link Double} value.
     * @see NumericInputType#maxSelectableNumericValue()
     */
    @Override
    public double maxSelectableNumericValue() {
        switch (this) {
            case CHILD_CM:
                return 120;

            case CHILD_FT:
                return 3;

            case TEEN_CM:
                return 250;

            case TEEN_FT:
                return 8;

            case TEEN_CM_DEC:
            case CHILD_CM_DEC:
                return 9;

            case TEEN_INCH:
            case CHILD_INCH:
                return 11;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }
}
