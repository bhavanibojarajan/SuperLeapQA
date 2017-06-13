package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.*;

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
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see #isToggleSettings(InputHelperType)
     * @see #switchSettingXP(InputHelperType)
     * @see #textSettingXP(InputHelperType)
     */
    @NotNull
    public XPath settingXP(@NotNull InputHelperType helper) {
        if (isToggleSettings(helper)) {
            return switchSettingXP(helper);
        } else {
            return textSettingXP(helper);
        }
    }

    /**
     * Check if the current {@link Setting} requires flipping a switch on
     * or off. If so, we need to locate the corresponding
     * {@link org.openqa.selenium.WebElement} differently.
     * @param helper {@link InputHelperType} instance.
     * @return {@link Boolean} value.
     * @see InputHelperType#platform()
     * @see Platform#ANDROID
     * @see #FOOD
     * @see #LOCATION
     */
    private boolean isToggleSettings(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

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
     * Get {@link CompoundAttribute} when
     * {@link #isToggleSettings(InputHelperType)} is false.
     * @param helper {@link InputHelperType} instance.
     * @return {@link CompoundAttribute} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see InputHelperType#localizer()
     * @see InputHelperType#platform()
     * @see LocalizerType#localize(String)
     * @see AndroidView.ViewType#TEXT_VIEW
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private CompoundAttribute textSettingAttribute(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();
        String clsName;

        if (platform.equals(Platform.ANDROID)) {
            clsName = AndroidView.ViewType.TEXT_VIEW.className();
        } else if (platform.equals(Platform.IOS)) {
            clsName = IOSView.ViewType.UI_STATIC_TEXT.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        LocalizerType localizer = helper.localizer();
        Attributes attrs = Attributes.of(platform);
        String localized = localizer.localize(title());

        return CompoundAttribute.builder()
            .addAttribute(attrs.containsText(localized))
            .withClass(clsName)
            .build();
    }

    /**
     * Get {@link XPath} query when {@link #isToggleSettings(InputHelperType)}
     * is false.
     * @param helper {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #textSettingAttribute(InputHelperType)
     */
    @NotNull
    private XPath textSettingXP(@NotNull InputHelperType helper) {
        CompoundAttribute cAttr = textSettingAttribute(helper);
        return XPath.builder().addAttribute(cAttr).build();
    }

    /**
     * Get {@link XPath} query when {@link #isToggleSettings(InputHelperType)}
     * is true.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see InputHelperType#platform()
     * @see XPath.Builder#followingSibling(CompoundAttribute, CompoundAttribute)
     * @see AndroidView.ViewType#SWITCH
     * @see IOSView.ViewType#UI_SWITCH
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #textSettingAttribute(InputHelperType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath switchSettingXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        String clsName;

        if (platform.equals(Platform.ANDROID)) {
            clsName = AndroidView.ViewType.SWITCH.className();
        } else if (platform.equals(Platform.IOS)) {
            clsName = IOSView.ViewType.UI_SWITCH.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        CompoundAttribute sAttr = CompoundAttribute.forClass(clsName);
        CompoundAttribute tAttr = textSettingAttribute(helper);
        return XPath.builder().followingSibling(sAttr, tAttr).build();
    }
}
