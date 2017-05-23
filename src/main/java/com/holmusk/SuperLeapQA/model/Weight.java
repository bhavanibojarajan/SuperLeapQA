package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLAndroidNumberPickerInputType;
import com.holmusk.SuperLeapQA.model.type.SLNumericChoiceInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements BaseErrorType, SLAndroidNumberPickerInputType {
    KG,
    KG_DEC,
    LB,
    LB_DEC;

    /**
     * Get the {@link Weight} instances for metric unit of measurement.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> metric(@NotNull PlatformType platform) {
        return Arrays.asList(KG, KG_DEC);
    }

    /**
     * Get the {@link Weight} instances for imperial unit of measurement.
     * @param platform {@link UserMode} instance.
     * @return {@link List} of {@link Weight}.
     */
    @NotNull
    public static List<Weight> imperial(@NotNull PlatformType platform) {
        return Arrays.asList(LB, LB_DEC);
    }

    /**
     * Get {@link List} of {@link Weight} instances, based on
     * {@link UserMode} and {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link List} of {@link Weight}.
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
     * Get {@link List} of random metric input values.
     * @param platform {@link PlatformType} instance.
     * @param MODE {@link UserMode} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link List} of {@link Zipped}.
     * @see #instances(PlatformType, UnitSystem)
     * @see SLNumericChoiceInputType#randomValue(UserMode)
     */
    @NotNull
    public static List<Zipped<Weight,String>> random(@NotNull PlatformType platform,
                                                     @NotNull final UserMode MODE,
                                                     @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> new Zipped<>(a, String.valueOf(a.randomValue(MODE))))
            .collect(Collectors.toList());
    }

    /**
     * @return {@link String} value.
     * @see SLNumericChoiceInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public String emptyInputErrorFormat() {
        return "register_error_weightValueNotSet";
    }

    /**
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current {@link Weight}. This
     * is useful for when there are multiple
     * {@link org.openqa.selenium.WebElement} with the same id (e.g. picking
     * {@link #KG} and {@link #KG_DEC} at the same time).
     * @return {@link Integer} value.
     * @see #NOT_AVAILABLE
     */
    @Override
    public int androidNumericPickerTargetItemIndex() {
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
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.InputType#inputViewXPath(PlatformType)
     * @see #androidInputViewXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXPath();

            case IOS:
                return iOSInputViewXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXPath() {
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

        return XPath.builder(Platform.ANDROID).containsID(ID).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        return XPath.builder(Platform.IOS).build();
    }

    /**
     * @return Return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType#choicePickerScrollViewXPath(PlatformType)
     * @see #androidScrollViewPickerXPath()
     * @see #iOSScrollViewPickerXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath choicePickerScrollViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidScrollViewPickerXPath();

            case IOS:
                return iOSScrollViewPickerXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see #androidNumericPickerTargetItemIndex()
     * @see XPath.Builder#atIndex(int)
     * @see XPath.Builder#ofClass(String)
     */
    @NotNull
    private XPath androidScrollViewPickerXPath() {
        String cls = "NumberPicker";
        int index = androidNumericPickerTargetItemIndex();
        return XPath.builder(Platform.ANDROID).atIndex(index).ofClass(cls).build();
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     */
    @NotNull
    private XPath iOSScrollViewPickerXPath() {
        return XPath.builder(Platform.IOS).build();
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType#choicePickerScrollViewItemXPath(PlatformType)
     * @see #androidScrollViewPickerItemXPath()
     * @see #iOSScrollViewPickerItemXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath choicePickerScrollViewItemXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidScrollViewPickerItemXPath();

            case IOS:
                return iOSScrollViewPickerItemXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scroll view picker item {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see #androidNumericPickerTargetItemIndex()
     * @see XPath.Builder#containsID(String)
     * @see XPath.Builder#ofInstance(int)
     */
    @NotNull
    private XPath androidScrollViewPickerItemXPath() {
        String id = "numberpicker_input";
        int index = androidNumericPickerTargetItemIndex();
        return XPath.builder(Platform.ANDROID).containsID(id).ofInstance(index).build();
    }

    /**
     * Get the scroll view picker item {@link XPath} for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     */
    @NotNull
    private XPath iOSScrollViewPickerItemXPath() {
        return XPath.builder(Platform.IOS).build();
    }

    /**
     * Get the appropriately formatted weight {@link String}, depending on
     * the type of {@link Weight}.
     * @param value {@link Integer} value.
     * @return {@link String} value.
     * @see SLNumericChoiceInputType#stringValue(int)
     */
    @NotNull
    @Override
    public String stringValue(int value) {
        return String.valueOf((int)value);
    }

    /**
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceInputType#minSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
     */
    @Override
    public int minSelectableNumericValue(@NotNull UserMode mode) {
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
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceInputType#maxSelectableNumericValue(UserMode)
     * @see UserMode#isParent()
     * @see #NOT_AVAILABLE
     */
    @Override
    public int maxSelectableNumericValue(@NotNull UserMode mode) {
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
