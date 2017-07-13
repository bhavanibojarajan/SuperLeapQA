package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMUnitSystemConvertibleType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height implements SLNumericChoiceType, HMUnitSystemConvertibleType {
    FT,
    INCH,
    CM,
    CM_DEC;

    //region Convenience Getters
    /**
     * Get the {@link Height} instances for {@link UnitSystem#METRIC}.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> metric(@NotNull PlatformType platform) {
        if (platform.equals(Platform.ANDROID)) {
            return HPIterables.asList(CM);
        } else {
            return HPIterables.asList(CM, CM_DEC);
        }
    }

    /**
     * Get the {@link Height} instances for {@link UnitSystem#IMPERIAL}.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link Height}.
     */
    @NotNull
    public static List<Height> imperial(@NotNull PlatformType platform) {
        return HPIterables.asList(FT, INCH);
    }

    /**
     * Get {@link List} of {@link Height} instances, based on
     * {@link Platform} and {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link List} of {@link Height}.
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
     * @return {@link List} of {@link Tuple}.
     * @see #instances(PlatformType, UnitSystem)
     */
    @NotNull
    public static List<Tuple<Height,String>> random(@NotNull PlatformType platform,
                                                    @NotNull final UserMode MODE,
                                                    @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> Tuple.of(a, String.valueOf(a.randomValue(MODE))))
            .collect(Collectors.toList());
    }

    /**
     * Get the {@link String} format to process a {@link List} of {@link Tuple}
     * {@link Height}.
     * @param platform {@link PlatformType} instance.
     * @return {@link String} value.
     */
    @NotNull
    public static String imperialFormat(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return "%.0f'%.0f\"";

            case IOS:
                return "%.0f ft %.0f in";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link String} representation of {@link Height} values, based
     * on {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @param inputs {@link List} of {@link Tuple}.
     * @return {@link String} value.
     * @see #imperialFormat(PlatformType)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static String stringValue(@NotNull PlatformType platform,
                                     @NotNull UnitSystem unit,
                                     @NotNull List<Tuple<Height,String>> inputs) {
        double a = Double.valueOf(inputs.get(0).B);
        double b = Double.valueOf(inputs.size() > 1 ? inputs.get(1).B : "0");

        switch (unit) {
            case METRIC:
                return String.format("%.0f.%.0f cm", a, b);

            case IMPERIAL:
                return String.format(imperialFormat(platform), a, b);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
    //endregion

    /**
     * Override this method to provide default implementation.
     * @return {@link UnitSystem} instance.
     * @see HMUnitSystemConvertibleType#unitSystem()
     */
    @NotNull
    @Override
    public UnitSystem unitSystem() {
        switch (this) {
            case FT:
            case INCH:
                return UnitSystem.IMPERIAL;

            default:
                return UnitSystem.METRIC;
        }
    }

    //region Picker Index
    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#androidScrollablePickerIndex(InputHelperType)
     */
    @Override
    public int androidScrollablePickerIndex(@NotNull InputHelperType helper) {
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
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#iOSScrollablePickerIndex(InputHelperType)
     */
    @Override
    public int iOSScrollablePickerIndex(@NotNull InputHelperType helper) {
        switch (this) {
            case FT:
            case CM:
                return 0;

            case INCH:
            case CM_DEC:
                return 2;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
    //endregion

    //region Input View XPath
    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(InputHelperType)
     */
    @NotNull
    @Override
    public XPath inputViewXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXP(helper);

            case IOS:
                return iOSInputViewXP(helper);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(Platform.ANDROID);

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

        Attribute attribute = attrs.containsID(ID);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(Platform.IOS);

        String text;

        switch (this) {
            case FT:
                text = "ft/in";
                break;

            case CM:
                text = "cm";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        Attribute attr = attrs.containsText(text);

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .addAttribute(attr)
            .withClass(IOSView.Type.UI_BUTTON)
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }
    //endregion

    //region Choice Values
    /**
     * Get the appropriately formatted height {@link String}, depending on
     * the type of {@link Height}.
     * @param helper {@link InputHelperType} instance.
     * @param value {@link Integer} value.
     * @return {@link String} value.
     * @see SLNumericChoiceType#stringValue(InputHelperType, double)
     */
    @NotNull
    @Override
    public String stringValue(@NotNull InputHelperType helper, double value) {
        return String.valueOf((int)value);
    }

    /**
     * Get the minimum selectable height.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#minSelectableValue(UserMode)
     */
    @Override
    public int minSelectableValue(@NotNull UserMode mode) {
        switch (this) {
            case INCH:
            case CM_DEC:
                return 1;

            case FT:
                return mode.isParent() ? 2 : 3;

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
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#maxSelectableValue(UserMode)
     */
    @Override
    public int maxSelectableValue(@NotNull UserMode mode) {
        switch (this) {
            case INCH:
                return 11;

            case CM_DEC:
                return 9;

            case FT:
                return mode.isParent() ? 3 : 6;

            case CM:
                return mode.isParent() ? 120 : 250;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
    //endregion
}
