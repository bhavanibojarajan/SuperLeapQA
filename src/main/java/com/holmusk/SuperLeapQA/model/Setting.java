package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/10/17.
 */
public enum Setting implements BaseErrorType {
    GOAL,
    FOOD,
    LOCATION,
    UNITS;

    /**
     * Get the {@link String} value as displayed by the
     * {@link org.openqa.selenium.WebElement} that corresponds to the current
     * {@link Setting}.
     * @return {@link String} value.
     * @see #GOAL
     * @see #FOOD
     * @see #LOCATION
     * @see #UNITS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case GOAL:
                return "settings_title_goal";

            case FOOD:
                return "settings_title_food";

            case LOCATION:
                return "settings_title_location";

            case UNITS:
                return "settings_title_units";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} query to locate the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link Setting}.
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see #isToggleSettings(PlatformType)
     * @see #switchSettingXP(PlatformType)
     * @see #textSettingXP(PlatformType)
     */
    @NotNull
    public XPath settingXP(@NotNull PlatformType platform) {
        if (isToggleSettings(platform)) {
            return switchSettingXP(platform);
        } else {
            return textSettingXP(platform);
        }
    }

    /**
     * Check if the current {@link Setting} requires flipping a switch on
     * or off. If so, we need to locate the corresponding
     * {@link org.openqa.selenium.WebElement} differently.
     * @param platform {@link PlatformType} instance.
     * @return {@link Boolean} value.
     * @see Platform#ANDROID
     * @see #FOOD
     * @see #LOCATION
     */
    private boolean isToggleSettings(@NotNull PlatformType platform) {
        switch (this) {
            case FOOD:
                return true;

            case LOCATION:
                /* This setting is only toggle-able on Android. On iOS, it is
                 * a static text element */
                if (platform.equals(Platform.ANDROID)) {
                    return true;
                } else {
                    break;
                }

            default:
                break;
        }

        return false;
    }

    /**
     * Get {@link CompoundAttribute} when {@link #isToggleSettings(PlatformType)}
     * is false.
     * @param platform {@link PlatformType} instance.
     * @return {@link CompoundAttribute} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(Attribute)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see LocalizerType#localize(String)
     * @see AndroidView.ViewType#TEXT_VIEW
     * @see Config#LOCALIZER
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private CompoundAttribute textSettingAttr(@NotNull PlatformType platform) {
        String clsName;

        if (platform.equals(Platform.ANDROID)) {
            clsName = AndroidView.ViewType.TEXT_VIEW.className();
        } else if (platform.equals(Platform.IOS)) {
            clsName = IOSView.ViewType.UI_STATIC_TEXT.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        LocalizerType localizer = Config.LOCALIZER;
        Attributes attrs = Attributes.of(platform);
        String localized = localizer.localize(title());

        return CompoundAttribute.builder()
            .addAttribute(attrs.containsText(localized))
            .withClass(clsName)
            .build();
    }

    /**
     * Get {@link XPath} query when {@link #isToggleSettings(PlatformType)}
     * is false.
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #textSettingAttr(PlatformType)
     */
    @NotNull
    private XPath textSettingXP(@NotNull PlatformType platform) {
        CompoundAttribute cAttr = textSettingAttr(platform);
        return XPath.builder().addAttribute(cAttr).build();
    }

    /**
     * Get {@link XPath} query when {@link #isToggleSettings(PlatformType)}
     * is true.
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see XPath.Builder#followingSibling(CompoundAttribute, CompoundAttribute)
     * @see AndroidView.ViewType#SWITCH
     * @see IOSView.ViewType#UI_SWITCH
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #textSettingAttr(PlatformType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath switchSettingXP(@NotNull PlatformType platform) {
        String clsName;

        if (platform.equals(Platform.ANDROID)) {
            clsName = AndroidView.ViewType.SWITCH.className();
        } else if (platform.equals(Platform.IOS)) {
            clsName = IOSView.ViewType.UI_SWITCH.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        CompoundAttribute sAttr = CompoundAttribute.forClass(clsName);
        CompoundAttribute tAttr = textSettingAttr(platform);
        return XPath.builder().followingSibling(sAttr, tAttr).build();
    }
}
