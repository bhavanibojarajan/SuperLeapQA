package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMUnitSystemConvertibleType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum Weight implements SLNumericChoiceType, HMUnitSystemConvertibleType {
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
     * @see #imperial(PlatformType)
     * @see #metric(PlatformType)
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
     */
    @NotNull
    public static List<Zip<Weight,String>> random(@NotNull PlatformType platform,
                                                  @NotNull final UserMode MODE,
                                                  @NotNull UnitSystem unit) {
        return instances(platform, unit).stream()
            .map(a -> Zip.of(a, String.valueOf(a.randomValue(MODE))))
            .collect(Collectors.toList());
    }

    /**
     * Get {@link String} representation of {@link Weight} values, based
     * on {@link UnitSystem}.
     * @param platform {@link PlatformType} instance.
     * @param unit {@link UnitSystem} instance.
     * @param inputs {@link List} of {@link Zip}.
     * @return {@link String} value.
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
     * Override this method to provide default implementation.
     * @return {@link UnitSystem} instance.
     * @see HMUnitSystemConvertibleType#unitSystem()
     */
    @NotNull
    @Override
    public UnitSystem unitSystem() {
        switch (this) {
            case LB:
            case LB_DEC:
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
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#iOSScrollablePickerIndex(InputHelperType)
     */
    @Override
    public int iOSScrollablePickerIndex(@NotNull InputHelperType helper) {
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
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(InputHelperType)
     * @see #androidInputViewXP(InputHelperType)
     * @see #iOSInputViewXP(InputHelperType)
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
            case KG:
                text = "kg";
                break;

            case LB:
                text = "lbs";
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
     * Get the appropriately formatted weight {@link String}, depending on
     * the type of {@link Weight}.
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
     * Override this method to provide default implementation.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     * @see SLNumericChoiceType#minSelectableValue(UserMode)
     */
    @Override
    public int minSelectableValue(@NotNull UserMode mode) {
        switch (this) {
            case KG_DEC:
            case LB_DEC:
                return 1;

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
     * @see SLNumericChoiceType#maxSelectableValue(UserMode)
     */
    @Override
    public int maxSelectableValue(@NotNull UserMode mode) {
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
