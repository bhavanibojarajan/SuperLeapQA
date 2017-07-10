package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.protocol.ClassNameProviderType;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 29/5/17.
 */
public enum CardType implements ErrorProviderType {
    ALL,
    MEAL,
    WEIGHT,
    ACTIVITY;

    /**
     * Get the displayed text on a {@link CardType} selector button.
     * @return {@link String} value.
     */
    @NotNull
    public String title() {
        switch (this) {
            case ALL:
                return "addCard_title_all";

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
     * Get the {@link String} title for the card that is displayed when there
     * are no logs.
     * @return {@link String} value.
     */
    @NotNull
    public String emptyItemPrompt() {
        return "addCard_title_logYour";
    }

    /**
     * Get {@link XPath} to locate the tab item corresponding to the current
     * {@link CardType} on
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see #title()
     */
    @NotNull
    public XPath cardTabXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();
        LocalizerType localizer = helper.localizer();
        Attributes attrs = Attributes.of(helper);
        String title = title();
        String localized = localizer.localize(title);
        ClassNameProviderType clsName;

        switch ((Platform)platform) {
            case ANDROID:
                clsName = AndroidView.Type.BUTTON;
                break;

            case IOS:
                clsName = IOSView.Type.UI_BUTTON;
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .withClass(clsName)
            .addAttribute(attrs.containsText(localized))
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }

    /**
     * Get {@link XPath} to locate the card icon
     * {@link org.openqa.selenium.WebElement}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see #androidIconXP(InputHelperType)
     * @see #iOSIconXP(InputHelperType)
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
     * Get the view id for each FAB button for {@link Platform#ANDROID}.
     * @return {@link String} value.
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
     * Get {@link XPath} to locate card icon for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#hasText(String)
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
            .withClass(IOSView.Type.UI_BUTTON)
            .addAttribute(attribute)
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }

    /**
     * Get {@link CardType} items.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see #androidCardItemXP(InputHelperType)
     * @see #iOSCardItemXP(InputHelperType)
     */
    @NotNull
    public XPath cardItemXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidCardItemXP(helper);

            case IOS:
                return iOSCardItemXP(helper);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link CardType} items for {@link Platform#ANDROID}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#containsText(String)
     * @see Axes#descendant(AttributeType)
     */
    @NotNull
    private XPath androidCardItemXP(@NotNull InputHelperType helper) {
        LocalizerType localizer = helper.localizer();
        String emptyTitle = emptyItemPrompt();
        String localized = localizer.localize(emptyTitle);
        Attributes attrs = Attributes.of(helper);

        return XPath.builder()
            .addAttribute(attrs.containsID("cv_item"))
            .addAttribute(Axes.descendant(CompoundAttribute.builder()
                .addAttribute(attrs.containsText(localized))
                .withWrapper(Wrapper.NOT)
                .build()
            ))
            .build();
    }

    /**
     * Get {@link CardType} items for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Axes#descendant(AttributeType)
     */
    @NotNull
    private XPath iOSCardItemXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();
        String emptyTitle = emptyItemPrompt();
        String localized = localizer.localize(emptyTitle);

        return XPath.builder()
            .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_COLLECTION_VIEW))
            .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_COLLECTION_VIEW_CELL))
            .addAttribute(Axes.descendant(CompoundAttribute.builder()
                .addAttribute(attrs.containsText(localized))
                .withWrapper(Wrapper.NOT)
                .build()
            ))
            .build();
    }
}
