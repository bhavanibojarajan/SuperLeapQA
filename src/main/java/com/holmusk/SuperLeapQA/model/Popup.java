package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.element.popup.PopupType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/19/17.
 */
public enum Popup implements PopupType, ErrorProviderType {
    MEAL_IMAGE_TUTORIAL,
    RATING;

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see PopupType#presenceXP(InputHelperType)
     * @see Attributes#containsText(String)
     */
    @NotNull
    @Override
    public XPath presenceXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();

        switch (this) {
            case MEAL_IMAGE_TUTORIAL:
                String mitLocalized = localizer.localize("mealPage_title_swipeToSeePhoto");
                Attribute mitAttr = attrs.containsText(mitLocalized);
                return XPath.builder().addAttribute(mitAttr).build();

            case RATING:
                String rtLocalized = localizer.localize("popup_title_enjoyingApp");
                Attribute rtAttr = attrs.containsText(rtLocalized);
                return XPath.builder().addAttribute(rtAttr).build();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see PopupType#dismissXP(InputHelperType)
     * @see Attributes#containsText(String)
     */
    @NotNull
    public XPath dismissXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(helper);
        LocalizerType localizer = helper.localizer();

        switch (this) {
            case MEAL_IMAGE_TUTORIAL:
                String mitLocalized = localizer.localize("mealPage_title_gotIt");
                Attribute mitAttr = attrs.containsText(mitLocalized);
                return XPath.builder().addAttribute(mitAttr).build();

            case RATING:
                String rtLocalized = localizer.localize("popup_title_notNow");
                Attribute rtAttr = attrs.containsText(rtLocalized);
                return XPath.builder().addAttribute(rtAttr).build();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param platform {@link PlatformType} instance.
     * @return {@link Boolean} value.
     * @see PopupType#applicableTo(PlatformType)
     */
    @Override
    public boolean applicableTo(@NotNull PlatformType platform) {
        switch (this) {
            case MEAL_IMAGE_TUTORIAL:
                return platform.equals(Platform.ANDROID);

            case RATING:
                return platform.equals(Platform.IOS);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
