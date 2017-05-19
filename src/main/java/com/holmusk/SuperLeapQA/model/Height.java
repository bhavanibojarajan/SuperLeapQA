package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Pair;
import org.swiften.xtestkit.base.element.action.input.type.NumericInputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements BaseErrorType, SLChoiceInputType, SLNumericInputType {
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
     * @return A {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> metric(@NotNull UserMode mode) {
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
     * @return A {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> imperial(@NotNull UserMode mode) {
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
     * @see NumericInputType#randomValue()
     */
    @NotNull
    public static List<Pair<Height,Double>> randomMetric(@NotNull UserMode mode) {
        return metric(mode).stream()
            .map(a -> new Pair<>(a, a.randomValue()))
            .collect(Collectors.toList());
    }

    /**
     * Get a {@link List} of random imperial input values.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Pair}.
     * @see #imperial(UserMode)
     * @see NumericInputType#randomValue()
     */
    @NotNull
    public static List<Pair<Height,Double>> randomImperial(@NotNull UserMode mode) {
        return imperial(mode).stream()
            .map(a -> new Pair<>(a, a.randomValue()))
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
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current {@link Height}. This
     * is useful for when there are multiple
     * {@link org.openqa.selenium.WebElement} with the same id (e.g. picking
     * {@link #CHILD_FT} and {@link #CHILD_INCH} at the same time).
     * @return An {@link Integer} value.
     * @see #NOT_IMPLEMENTED
     */
    public int androidViewIndex() {
        switch (this) {
            case CHILD_FT:
            case CHILD_CM:
            case TEEN_FT:
            case TEEN_CM:
                return 0;

            case CHILD_INCH:
            case CHILD_CM_DEC:
            case TEEN_INCH:
            case TEEN_CM_DEC:
                return 1;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #newXPathBuilder()
     */
    @NotNull
    @Override
    public XPath androidViewXPath() {
        final String ID;

        switch (this) {
            case CHILD_FT:
            case CHILD_INCH:
            case TEEN_FT:
            case TEEN_INCH:
                ID = "btn_ft";
                break;

            case CHILD_CM:
            case CHILD_CM_DEC:
            case TEEN_CM:
            case TEEN_CM_DEC:
                ID = "btn_cm";
                break;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }

        return newXPathBuilder().containsID(ID).build();
    }

    /**
     * @return Return a {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewPickerXPath()
     * @see #newXPathBuilder()
     * @see #androidViewIndex()
     */
    @NotNull
    @Override
    public XPath androidScrollViewPickerXPath() {
        String cls = "NumberPicker";
        int index = androidViewIndex();
        return newXPathBuilder().atIndex(index).ofClass(cls).build();
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #newXPathBuilder()
     * @see #androidViewIndex()
     */
    @Override
    public XPath androidScrollViewItemXPath() {
        String id = "numberpicker_input";
        int index = androidViewIndex();
        return newXPathBuilder().containsID(id).ofInstance(index).build();
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
