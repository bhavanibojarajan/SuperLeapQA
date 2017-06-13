package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 12/6/17.
 */
public enum CSSInput implements HMCSSInputType {
    WEIGHT,
    ACTIVITY;

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see HMCSSInputType#CSSXP(InputHelperType)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see InputHelperType#platform()
     * @see XPath.Builder#addAttribute(Attribute)
     * @see Platform#ANDROID
     * @see #androidCSSId()
     */
    @NotNull
    @Override
    public XPath CSSXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        if (platform.equals(Platform.ANDROID)) {
            Attributes attrs = Attributes.of(platform);
            String id = androidCSSId();
            Attribute attr = attrs.containsID(id);
            return XPath.builder().addAttribute(attr).build();
        } else {
            return HMCSSInputType.super.CSSXP(helper);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see HMCSSInputType#CSSValueDisplayXP(InputHelperType)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see InputHelperType#platform()
     * @see XPath.Builder#addAttribute(Attribute)
     * @see Platform#ANDROID
     * @see #androidCSSValueDisplayId()
     */
    @NotNull
    @Override
    public XPath CSSValueDisplayXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        if (platform.equals(Platform.ANDROID)) {
            Attributes attrs = Attributes.of(platform);
            String id = androidCSSValueDisplayId();
            Attribute attr = attrs.containsID(id);
            return XPath.builder().addAttribute(attr).build();
        } else {
            return HMCSSInputType.super.CSSValueDisplayXP(helper);
        }
    }

    /**
     * Get the CSS id for {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see #ACTIVITY
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String androidCSSId() {
        switch (this) {
            case ACTIVITY:
                return "bis_add_activity";

            case WEIGHT:
                return "bis_add_weight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the CSS value display id for {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see #ACTIVITY
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String androidCSSValueDisplayId() {
        switch (this) {
            case ACTIVITY:
                return "tv_add_activity_tag_activityvalue";

            case WEIGHT:
                return "tv_add_weight_tag_weightvalue";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
