package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLChoiceInputType;
import com.holmusk.SuperLeapQA.model.type.SLNumericInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zipped;
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
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link Height}.
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
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> imperial(@NotNull PlatformType platform) {
        return Arrays.asList(FT, INCH);
    }

    /**
     * Get {@link List} of {@link Height} instances, based on
     * {@link Platform} and {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link List} of {@link Height}.
     * @see #metric(PlatformType)
     * @see #imperial(PlatformType)
     * @see #NOT_AVAILABLE
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
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link List} of random metric input values.
     * @param platform {@link PlatformType} instance.
     * @param MODE {@link UserMode} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link List} of {@link Zipped}.
     * @see #instances(PlatformType, UnitSystem)
     * @see SLNumericInputType#randomValue(UserMode)
     */
    @NotNull
    public static List<Zipped<Height,Double>> random(@NotNull PlatformType platform,
                                                     @NotNull final UserMode MODE,
                                                     @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> new Zipped<>(a, a.randomValue(MODE)))
            .collect(Collectors.toList());
    }

    /**
     * Get {@link String} representation of {@link Height} values, based
     * on {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @param inputs {@link List} of {@link Zipped}.
     * @return {@link String} value.
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static String stringValue(@NotNull PlatformType platform,
                                     @NotNull UnitSystem unit,
                                     @NotNull List<Zipped<Height,Double>> inputs) {
        double a = inputs.get(0).B;
        double b = inputs.size() > 1 ? inputs.get(1).B : 0;

        switch (unit) {
            case METRIC:
                return String.format("%.0f.%.0f cm", a, b);

            case IMPERIAL:
                return String.format("%.0f'%.0f\"", a, b);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @return {@link String} value.
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
     * @return {@link Integer} value.
     * @see SLChoiceInputType#androidPickerItemIndex()
     * @see #NOT_AVAILABLE
     */
    @Override
    public int androidPickerItemIndex() {
        switch (this) {
            case FT:
            case CM:
                return 0;

            case INCH:
            case CM_DEC:
                return 1;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @return {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #NOT_AVAILABLE
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
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return xPathBuilder().containsID(ID).build();
    }

    /**
     * @return Return {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewPickerXPath()
     * @see #xPathBuilder()
     * @see #androidPickerItemIndex()
     */
    @NotNull
    @Override
    public XPath androidScrollViewPickerXPath() {
        String cls = "NumberPicker";
        int index = androidPickerItemIndex();
        return xPathBuilder().atIndex(index).ofClass(cls).build();
    }

    /**
     * @return {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #xPathBuilder()
     * @see #androidPickerItemIndex()
     */
    @NotNull
    @Override
    public XPath androidScrollViewItemXPath() {
        String id = "numberpicker_input";
        int index = androidPickerItemIndex();
        return xPathBuilder().containsID(id).ofInstance(index).build();
    }

    /**
     * Get the appropriately formatted height {@link String}, depending on
     * the type of {@link Height}.
     * @param value {@link Double} value.
     * @return {@link String} value.
     * @see SLNumericInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
        return String.valueOf((int)value);
    }

    /**
     * Get the minimum selectable height.
     * @param mode {@link UserMode} instance.
     * @return {@link Double} value.
     * @see SLNumericInputType#minSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
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
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the maximum selectable height. Return a lower value to avoid
     * {@link StackOverflowError} from too much scrolling.
     * @param mode {@link UserMode} instance.
     * @return {@link Double} value.
     * @see SLNumericInputType#maxSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
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
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
