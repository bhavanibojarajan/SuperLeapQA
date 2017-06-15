package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 29/5/17.
 */
public enum CardType implements BaseErrorType {
    MEAL,
    WEIGHT,
    ACTIVITY;

    /**
     * Get the displayed text on a {@link CardType} selector button.
     * @return {@link String} value.
     * @see #ACTIVITY
     * @see #MEAL
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case ACTIVITY:
                return "addCard_title_activity";

            case MEAL:
                return "addCard_title_food";

            case WEIGHT:
                return "addCard_title_weight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the view id for each FAB button for {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see Platform#ANDROID
     * @see #ACTIVITY
     * @see #MEAL
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case ACTIVITY:
                return "fab_activity";

            case MEAL:
                return "fab_food";

            case WEIGHT:
                return "fab_weight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} to locate the card icon
     * {@link org.openqa.selenium.WebElement}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see #androidIconXP(InputHelperType)
     * @see #iOSIconXP(InputHelperType)
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath cardIconXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidIconXP(helper);

            case IOS:
                return iOSIconXP(helper);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} to locate card icon for {@link Platform#ANDROID}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformProviderType)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #androidViewId()
     */
    @NotNull
    private XPath androidIconXP(@NotNull InputHelperType helper) {
        String id = androidViewId();
        Attributes attrs = Attributes.of(helper);
        Attribute attribute = attrs.containsID(id);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} to locate card icon for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see InputHelperType#localizer()
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.ViewType#UI_BUTTON
     * @see #title()
     */
    @NotNull
    private XPath iOSIconXP(@NotNull InputHelperType helper) {
        String title = title();
        LocalizerType localizer = helper.localizer();
        String localized = localizer.localize(title);
        Attributes attrs = Attributes.of(helper);
        Attribute attribute = attrs.containsText(localized);

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .withClass(IOSView.ViewType.UI_BUTTON.className())
            .addAttribute(attribute)
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }
}
