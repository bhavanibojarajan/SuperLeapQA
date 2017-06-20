package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.ViewType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 29/5/17.
 */
public enum CardType implements BaseErrorType {
    ALL,
    MEAL,
    WEIGHT,
    ACTIVITY;

    /**
     * Get the displayed text on a {@link CardType} selector button.
     * @return {@link String} value.
     * @see #ALL
     * @see #ACTIVITY
     * @see #MEAL
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
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
     * @see Attributes#of(PlatformProviderType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see ViewType#className()
     * @see InputHelperType#localizer()
     * @see InputHelperType#platform()
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see AndroidView.Type#BUTTON
     * @see IOSView.Type#UI_BUTTON
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #title()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath cardTabXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();
        LocalizerType localizer = helper.localizer();
        Attributes attrs = Attributes.of(helper);
        String title = title();
        String localized = localizer.localize(title);
        String clsName;

        switch ((Platform)platform) {
            case ANDROID:
                clsName = AndroidView.Type.BUTTON.className();
                break;

            case IOS:
                clsName = IOSView.Type.UI_BUTTON.className();
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
     * Get {@link XPath} to locate card icon for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#hasText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see ViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see InputHelperType#localizer()
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.Type#UI_BUTTON
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
            .withClass(IOSView.Type.UI_BUTTON.className())
            .addAttribute(attribute)
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }

    /**
     * Get {@link CardType} items.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see InputHelperType#platform()
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #androidCardItemXP(InputHelperType)
     * @see #iOSCardItemXP(InputHelperType)
     * @see #NOT_AVAILABLE
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
     * @see Attribute#not()
     * @see Attributes#containsID(String)
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see Axes#descendant(AttributeType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withWrapper(Wrapper)
     * @see InputHelperType#localizer()
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see Joiner#AND
     * @see Wrapper#NOT
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
     * @see Attribute#not()
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see Axes#descendant(AttributeType)
     * @see CompoundAttribute#forClass(String)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see CompoundAttribute.Builder#withWrapper(Wrapper)
     * @see ViewType#className()
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.Type#UI_COLLECTION_VIEW
     * @see IOSView.Type#UI_COLLECTION_VIEW_CELL
     * @see Wrapper#NOT
     */
    @NotNull
    private XPath iOSCardItemXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();
        String emptyTitle = emptyItemPrompt();
        String localized = localizer.localize(emptyTitle);
        String clvClass = IOSView.Type.UI_COLLECTION_VIEW.className();
        String cllClass = IOSView.Type.UI_COLLECTION_VIEW_CELL.className();

        return XPath.builder()
            .addAttribute(CompoundAttribute.forClass(clvClass))
            .addAttribute(CompoundAttribute.forClass(cllClass))
            .addAttribute(Axes.descendant(CompoundAttribute.builder()
                .addAttribute(attrs.containsText(localized))
                .withWrapper(Wrapper.NOT)
                .build()
            ))
            .build();
    }
}
