package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
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
     * @param engine {@link Engine} instance.
     * @return {@link XPath} instance.
     * @see HMCSSInputType#CSSXP(Engine)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see XPath.Builder#addAttribute(Attribute)
     * @see Platform#ANDROID
     * @see #androidCSSId()
     */
    @NotNull
    @Override
    public XPath CSSXP(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attributes attrs = Attributes.of(Platform.ANDROID);
            String id = androidCSSId();
            Attribute attr = attrs.containsID(id);
            return XPath.builder().addAttribute(attr).build();
        } else {
            return HMCSSInputType.super.CSSXP(engine);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param engine {@link Engine} instance.
     * @return {@link XPath} instance.
     * @see HMCSSInputType#CSSValueDisplayXP(Engine)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see XPath.Builder#addAttribute(Attribute)
     * @see Platform#ANDROID
     * @see #androidCSSValueDisplayId()
     */
    @NotNull
    @Override
    public XPath CSSValueDisplayXP(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attributes attrs = Attributes.of(Platform.ANDROID);
            String id = androidCSSValueDisplayId();
            Attribute attr = attrs.containsID(id);
            return XPath.builder().addAttribute(attr).build();
        } else {
            return HMCSSInputType.super.CSSValueDisplayXP(engine);
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
                return "tv_add_weight_tag_activityvalue";

            case WEIGHT:
                return "tv_add_weight_tag_weightvalue";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
