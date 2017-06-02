package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLNumericChoiceInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.ios.IOSView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements SLNumericChoiceInputType {
    KG,
    KG_DEC,
    LB,
    LB_DEC;

    //region Convenience Getters
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
     * @return {@link List} of {@link Zip}.
     * @see #instances(PlatformType, UnitSystem)
     * @see SLNumericChoiceInputType#randomValue(UserMode)
     */
    @NotNull
    public static List<Zip<Weight,String>> random(@NotNull PlatformType platform,
                                                  @NotNull final UserMode MODE,
                                                  @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> new Zip<>(a, String.valueOf(a.randomValue(MODE))))
            .collect(Collectors.toList());
    }

    /**
     * Get {@link String} representation of {@link Weight} values, based
     * on {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @param inputs {@link List} of {@link Zip}.
     * @return {@link String} value.
     * @see UnitSystem#METRIC
     * @see UnitSystem#IMPERIAL
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static String stringValue(@NotNull PlatformType platform,
                                     @NotNull UnitSystem unit,
                                     @NotNull List<Zip<Weight,String>> inputs) {
        double a = Double.valueOf(inputs.get(0).B);
        double b = Double.valueOf(inputs.size() > 1 ? inputs.get(1).B : "0");

        switch (unit) {
            case METRIC:
                return String.format("%.0f.%.0f kg", a, b);

            case IMPERIAL:
                return String.format("%.0f.%.0f lbs", a, b);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
    //endregion

    /**
     * @return {@link String} value.
     * @see SLNumericChoiceInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public String emptyInputErrorFormat() {
        return "register_error_weightValueNotSet";
    }

    //region Picker Index
    /**
     * @return {@link Integer} value.
     * @see SLNumericChoiceInputType#androidScrollablePickerIndex()
     * @see #NOT_AVAILABLE
     */
    @Override
    public int androidScrollablePickerIndex() {
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
     * @return {@link Integer} value.
     * @see SLNumericChoiceInputType#iOSScrollablePickerIndex()
     * @see #NOT_AVAILABLE
     */
    @Override
    public int iOSScrollablePickerIndex() {
        switch (this) {
            case KG:
            case LB:
                return 0;

            case KG_DEC:
            case LB_DEC:
                return 2;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
    //endregion

    //region Input View XPath
    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXPath(PlatformType)
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
     * @see IOSView.ViewType#UI_BUTTON
     * @see XPath.Builder#setClass(String)
     * @see XPath.Builder#containsText(XPath.ContainsText)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        String text;

        switch (this) {
            case KG:
                text = "kg";
                break;

            case LB:
                text = "lbs";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return XPath.builder(Platform.IOS)
            .setClass(IOSView.ViewType.UI_BUTTON.className())
            .containsText(text)
            .build();
    }
    //endregion

    //region Choice Values
    /**
     * Get the appropriately formatted weight {@link String}, depending on
     * the type of {@link Weight}.
     * @param value {@link Integer} value.
     * @return {@link String} value.
     * @see SLNumericChoiceInputType#stringValue(double)
     */
    @NotNull
    @Override
    public String stringValue(double value) {
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
    //endregion
}
