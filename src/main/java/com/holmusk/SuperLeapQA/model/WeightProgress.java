package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.ViewType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 20/6/17.
 */
public enum WeightProgress implements BaseErrorType {
    PREVIOUS,
    CHANGE;

    /**
     * Get the title {@link String} corresponding to the current
     * {@link WeightProgress}.
     * @return {@link String} value.
     * @see #CHANGE
     * @see #PREVIOUS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case PREVIOUS:
                return "weightPage_title_previous";

            case CHANGE:
                return "weightPage_title_change";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} that can be used to locate the value of the current
     * {@link WeightProgress}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see InputHelperType#platform()
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #androidValueXP(InputHelperType)
     * @see #iOSValueXP(InputHelperType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath valueXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidValueXP(helper);

            case IOS:
                return iOSValueXP(helper);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the value {@link XPath} for {@link Platform#ANDROID}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformProviderType)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #androidValueId()
     */
    @NotNull
    private XPath androidValueXP(@NotNull InputHelperType helper) {
        String id = androidValueId();
        Attributes attrs = Attributes.of(helper);
        Attribute attribute = attrs.containsID(id);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get the id of {@link org.openqa.selenium.WebElement} corresponding to
     * the current {@link WeightProgress}.
     * @return {@link String} value.
     * @see #CHANGE
     * @see #PREVIOUS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String androidValueId() {
        switch (this) {
            case PREVIOUS:
                return "tv_item_activity_weight_detail_valueleft";

            case CHANGE:
                return "tv_item_activity_weight_detail_valuemid";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the value {@link XPath} for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see Axes#child(AttributeType)
     * @see Axes#followingSibling(AttributeType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see CompoundAttribute#empty()
     * @see ViewType#className()
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see IOSView.Type#UI_STATIC_TEXT
     * @see IOSView.Type#UNDEFINED
     */
    @NotNull
    private XPath iOSValueXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();
        String title = title();
        String localized = localizer.localize(title);
        String otherCls = IOSView.Type.UNDEFINED.className();
        String sttCls = IOSView.Type.UI_STATIC_TEXT.className();
        CompoundAttribute otherCAttr = CompoundAttribute.forClass(otherCls);

        return XPath.builder()
            .addAttribute(CompoundAttribute.builder()
                .withClass(sttCls)
                .addAttribute(attrs.containsText(localized))
                .build())
            .addAttribute(Axes.followingSibling(otherCAttr))
            .addAttribute(Axes.child(CompoundAttribute.empty()))
            .build();
    }
}
