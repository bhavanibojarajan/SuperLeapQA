package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Pair;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements BaseErrorType, SLChoiceInputType, SLNumericInputType {
    FT,
    INCH,
    CM,
    CM_DEC;

    /**
     * Get the {@link Height} instances for {@link UnitSystem#METRIC}.
     * @param platform A {@link PlatformType} instance.
     * @return A {@link List} of {@link Height}.
     * @see Platform#ANDROID
     */
    @NotNull
    public static List<Height> metric(@NotNull PlatformType platform) {
        if (platform.equals(Platform.ANDROID)) {
            return Collections.singletonList(CM);
        } else {
            return Arrays.asList(CM, CM_DEC);
        }
    }

    /**
     * Get the {@link Height} instances for {@link UnitSystem#IMPERIAL}.
     * @param platform A {@link PlatformType} instance.
     * @return A {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> imperial(@NotNull PlatformType platform) {
        return Arrays.asList(FT, INCH);
    }

    /**
     * Get a {@link List} of {@link Height} instances, based on
     * {@link Platform} and {@link UnitSystem}.
     * @param platform A {@link PlatformType} instance.
     * @param unit A {@link UnitSystem} instance.
     * @return A {@link List} of {@link Height}.
     * @see #metric(PlatformType)
     * @see #imperial(PlatformType)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    public static List<Height> instances(@NotNull PlatformType platform,
                                         @NotNull UnitSystem unit) {
        switch (unit) {
            case METRIC:
                return metric(platform);

            case IMPERIAL:
                return imperial(platform);

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get a {@link List} of random metric input values.
     * @param platform A {@link PlatformType} instance.
     * @param MODE A {@link UserMode} instance.
     * @param unit A {@link UnitSystem} instance.
     * @return A {@link List} of {@link Pair}.
     * @see #instances(PlatformType, UnitSystem)
     * @see SLNumericInputType#randomValue(UserMode)
     */
    @NotNull
    public static List<Pair<Height,Double>> random(@NotNull PlatformType platform,
                                                   @NotNull final UserMode MODE,
                                                   @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> new Pair<>(a, a.randomValue(MODE)))
            .collect(Collectors.toList());
    }

    /**
     * Get a {@link String} representation of {@link Height} values, based
     * on {@link UnitSystem}.
     * @param platform A {@link PlatformType} instance.
     * @param unit A {@link UnitSystem} instance.
     * @param inputs A {@link List} of {@link Pair}.
     * @return A {@link String} value.
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static String stringValue(@NotNull PlatformType platform,
                                     @NotNull UnitSystem unit,
                                     @NotNull List<Pair<Height,Double>> inputs) {
        double a = inputs.get(0).B;
        double b = inputs.size() > 1 ? inputs.get(1).B : 0;

        switch (unit) {
            case METRIC:
                return String.format("%.0f.%.0f cm", a, b);

            case IMPERIAL:
                return String.format("%.0f'%.0f\"", a, b);

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
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
     * {@link #FT} and {@link #INCH} at the same time).
     * @return An {@link Integer} value.
     * @see #NOT_IMPLEMENTED
     */
    public int androidViewIndex() {
        switch (this) {
            case FT:
            case CM:
                return 0;

            case INCH:
            case CM_DEC:
                return 1;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    @Override
    public XPath androidViewXPath() {
        final String ID;

        switch (this) {
            case CM:
            case CM_DEC:
                ID = "btn_cm";
                break;

            case FT:
            case INCH:
                ID = "btn_ft";
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
    @NotNull
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
     * @see SLNumericInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        return String.valueOf((int)value);
    }

    /**
     * Get the minimum selectable height.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Double} value.
     * @see SLNumericInputType#minSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_IMPLEMENTED
     */
    @Override
    public double minSelectableNumericValue(@NotNull UserMode mode) {
        switch (this) {
            case INCH:
            case CM_DEC:
                return 0;

            case FT:
                return mode.isParent() ? 1 : 3;

            case CM:
                return mode.isParent() ? 50 : 120;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the maximum selectable height. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Double} value.
     * @see SLNumericInputType#maxSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_IMPLEMENTED
     */
    @Override
    public double maxSelectableNumericValue(@NotNull UserMode mode) {
        switch (this) {
            case INCH:
                return 11;

            case CM_DEC:
                return 9;

            case FT:
                return mode.isParent() ? 3 : 8;

            case CM:
                return mode.isParent() ? 120 : 250;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }
}
