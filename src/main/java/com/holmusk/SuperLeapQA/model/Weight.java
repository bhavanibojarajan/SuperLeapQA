package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements BaseErrorType, SLChoiceInputType, SLNumericInputType {
    KG,
    KG_DEC,
    LB,
    LB_DEC;

    /**
     * Get the {@link Weight} instances for metric unit of measurement.
     * @param platform A {@link PlatformType} instance.
     * @return A {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> metric(@NotNull PlatformType platform) {
        return Arrays.asList(KG, KG_DEC);
    }

    /**
     * Get the {@link Weight} instances for imperial unit of measurement.
     * @param platform A {@link UserMode} instance.
     * @return A {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> imperial(@NotNull PlatformType platform) {
        return Arrays.asList(LB, LB_DEC);
    }

    /**
     * Get a {@link List} of {@link Weight} instances, based on
     * {@link UserMode} and {@link UnitSystem}.
     * @param platform A {@link PlatformType} instance.
     * @param unit A {@link UnitSystem} instance.
     * @return A {@link List} of {@link Weight}.
     * @see #metric(PlatformType)
     * @see #imperial(PlatformType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public static List<Weight> instances(@NotNull PlatformType platform,
                                         @NotNull UnitSystem unit) {
        switch (unit) {
            case METRIC:
                return metric(platform);

            case IMPERIAL:
                return imperial(platform);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get a {@link List} of random metric input values.
     * @param platform A {@link PlatformType} instance.
     * @param MODE A {@link UserMode} instance.
     * @param unit A {@link UnitSystem} instance.
     * @return A {@link List} of {@link Zipped}.
     * @see #instances(PlatformType, UnitSystem)
     * @see SLNumericInputType#randomValue(UserMode)
     */
    @NotNull
    public static List<Zipped<Weight,Double>> random(@NotNull PlatformType platform,
                                                     @NotNull final UserMode MODE,
                                                     @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> new Zipped<>(a, a.randomValue(MODE)))
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
     * {@link #KG} and {@link #KG_DEC} at the same time).
     * @return An {@link Integer} value.
     * @see #NOT_AVAILABLE
     */
    public int androidViewIndex() {
        switch (this) {
            case KG:
            case LB:
                return 0;

            case KG_DEC:
            case LB_DEC:
                return 1;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #newXPathBuilder()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath androidViewXPath() {
        final String ID;

        switch (this) {
            case LB:
            case LB_DEC:
                ID = "btn_lb";
                break;

            case KG:
            case KG_DEC:
                ID = "btn_kg";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
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
     * @see SLNumericInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        return String.valueOf((int)value);
    }

    /**
     * @param mode A {@link UserMode} instance.
     * @return A {@link Double} value.
     * @see SLNumericInputType#minSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
     */
    @Override
    public double minSelectableNumericValue(@NotNull UserMode mode) {
        switch (this) {
            case KG_DEC:
            case LB_DEC:
                return 0;

            case LB:
                return mode.isParent() ? 22 : 110;

            case KG:
                return mode.isParent() ? 10 : 50;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the maximum selectable weight. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Double} value.
     * @see SLNumericInputType#maxSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
     */
    @Override
    public double maxSelectableNumericValue(@NotNull UserMode mode) {
        switch (this) {
            case KG_DEC:
            case LB_DEC:
                return 9;

            case KG:
                return mode.isParent() ? 80 : 250;

            case LB:
                return mode.isParent() ? 176 : 485;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
