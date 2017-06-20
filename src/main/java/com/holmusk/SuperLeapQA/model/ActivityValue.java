package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 15/6/17.
 */
public enum ActivityValue implements BaseErrorType {
    TODAY,
    WEEKLY_GOAL;

    /**
     * Get the associated title.
     * @return {@link String} value.
     * @see #TODAY
     * @see #WEEKLY_GOAL
     */
    @NotNull
    public String title() {
        switch (this) {
            case TODAY:
                return "dashboard_activity_title_today";

            case WEEKLY_GOAL:
                return "dashboard_activity_title_weeklyGoal";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} instance that will be used to locate the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link ActivityValue}.
     * @param helper {@link InputHelperType} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link XPath} instance.
     * @see InputHelperType#platform()
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #androidValueXP(InputHelperType, UserMode)
     * @see #iOSValueXP(InputHelperType, UserMode)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath valueXP(@NotNull InputHelperType helper, @NotNull UserMode mode) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidValueXP(helper, mode);

            case IOS:
                return iOSValueXP(helper, mode);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the id of the value {@link org.openqa.selenium.WebElement} for
     * {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see #TODAY
     * @see #WEEKLY_GOAL
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String androidValueId() {
        switch (this) {
            case TODAY:
                return "todayValue_activity";

            case WEEKLY_GOAL:
                return "goalValue_activity";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the value {@link XPath} for {@link Platform#ANDROID}
     * @param helper {@link InputHelperType} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformProviderType)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #androidValueId()
     */
    @NotNull
    private XPath androidValueXP(@NotNull InputHelperType helper,
                                 @NotNull UserMode mode) {
        String id = androidValueId();
        Attributes attrs = Attributes.of(helper);
        Attribute attribute = attrs.containsID(id);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get the value {@link XPath} for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link XPath} instance.
     * @see Attribute#not()
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see Axes#followingSibling(AttributeType)
     * @see LocalizerType#localize(String)
     * @see InputHelperType#localizer()
     * @see UserMode#dashboardActivityKeyword()
     * @see #title()
     */
    @NotNull
    private XPath iOSValueXP(@NotNull InputHelperType helper,
                             @NotNull UserMode mode) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();
        String today = localizer.localize(title());
        String keyword = localizer.localize(mode.dashboardActivityKeyword());
        Attribute notAttr = attrs.containsText(keyword).not();

        return XPath.builder()
            .addAttribute(attrs.containsText(today))
            .addAttribute(Axes.followingSibling(notAttr))
            .build();
    }
}
