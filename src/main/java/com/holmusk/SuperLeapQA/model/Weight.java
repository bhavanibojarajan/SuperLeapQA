package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Pair;
import org.swiften.xtestkit.base.element.action.input.type.NumericInputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements BaseErrorType, SLChoiceInputType, SLNumericInputType {
    CHILD_KG,
    CHILD_KG_DEC,
    CHILD_LB,
    CHILD_LB_DEC,
    TEEN_KG,
    TEEN_KG_DEC,
    TEEN_LB,
    TEEN_LB_DEC;

    /**
     * Get the {@link Weight} instances for metric unit of measurement.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> metric(@NotNull UserMode mode) {
        switch (mode) {
            case PARENT:
                return Arrays.asList(CHILD_KG, CHILD_KG_DEC);

            case TEEN_ABOVE_18:
            case TEEN_UNDER_18:
                return Arrays.asList(TEEN_KG, TEEN_KG_DEC);

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the {@link Weight} instances for imperial unit of measurement.
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> imperial(@NotNull UserMode mode) {
        switch (mode) {
            case PARENT:
                return Arrays.asList(CHILD_LB, CHILD_LB_DEC);

            case TEEN_ABOVE_18:
            case TEEN_UNDER_18:
                return Arrays.asList(TEEN_LB, TEEN_LB_DEC);

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
    public static List<Pair<Weight,Double>> randomMetric(@NotNull UserMode mode) {
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
    public static List<Pair<Weight,Double>> randomImperial(@NotNull UserMode mode) {
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
        return "register_error_weightValueNotSet";
    }

    @NotNull
    @Override
    public Converter<Double> converter() {
        return a -> a;
    }

    /**
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current {@link Weight}. This
     * is useful for when there are multiple
     * {@link org.openqa.selenium.WebElement} with the same id (e.g. picking
     * {@link #CHILD_KG} and {@link #CHILD_KG_DEC} at the same time).
     * @return An {@link Integer} value.
     * @see #NOT_IMPLEMENTED
     */
    public int androidViewIndex() {
        switch (this) {
            case CHILD_KG:
            case CHILD_LB:
            case TEEN_KG:
            case TEEN_LB:
                return 0;

            case CHILD_KG_DEC:
            case CHILD_LB_DEC:
            case TEEN_KG_DEC:
            case TEEN_LB_DEC:
                return 1;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link XPath} value.
     * @see #newXPathBuilder()
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    public XPath androidViewXPath() {
        final String ID;

        switch (this) {
            case CHILD_LB:
            case CHILD_LB_DEC:
            case TEEN_LB:
            case TEEN_LB_DEC:
                ID = "btn_lb";
                break;

            case CHILD_KG:
            case CHILD_KG_DEC:
            case TEEN_KG:
            case TEEN_KG_DEC:
                ID = "btn_kg";
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
     * Get the appropriately formatted weight {@link String}, depending on
     * the type of {@link Weight}.
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
     * Get the minimum selectable weight.
     * @return A {@link Double} value.
     * @see NumericInputType#minSelectableNumericValue()
     */
    @Override
    public double minSelectableNumericValue() {
        switch (this) {
            case CHILD_KG:
                return 10;

            case CHILD_LB:
                return 22;

            case TEEN_KG:
                return 50;

            case TEEN_LB:
                return 110;

            case CHILD_KG_DEC:
            case TEEN_KG_DEC:
            case CHILD_LB_DEC:
            case TEEN_LB_DEC:
                return 0;

            default:
                return 0;
        }
    }

    /**
     * Get the maximum selectable weight. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @return A {@link Double} value.
     * @see NumericInputType#maxSelectableNumericValue()
     */
    @Override
    public double maxSelectableNumericValue() {
        switch (this) {
            case CHILD_KG:
                return 80;

            case CHILD_LB:
                return 176;

            case TEEN_KG:
                return 250;

            case TEEN_LB:
                return 485;

            case CHILD_KG_DEC:
            case TEEN_KG_DEC:
            case CHILD_LB_DEC:
            case TEEN_LB_DEC:
                return 9;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }
}
