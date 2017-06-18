package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.param.ByXPath;
import org.swiften.xtestkit.base.element.popup.PopupType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.AttributeType;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/19/17.
 */
public enum Popup implements PopupType, BaseErrorType {
    RATING;

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see PopupType#presenceXP(InputHelperType)
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see BooleanUtil#toTrue(Object)
     * @see InputHelperType#localizer()
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #RATING
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath presenceXP(@NotNull InputHelperType helper) {
        switch (this) {
            case RATING:
                Attributes attrs = Attributes.of(helper);
                LocalizerType localizer = helper.localizer();
                String localized = localizer.localize("popup_title_enjoyingApp");
                Attribute attribute = attrs.containsText(localized);
                return XPath.builder().addAttribute(attribute).build();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see PopupType#dismissXP(InputHelperType)
     * @see PopupType#presenceXP(InputHelperType)
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see BooleanUtil#toTrue(Object)
     * @see InputHelperType#localizer()
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #RATING
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath dismissXP(@NotNull InputHelperType helper) {
        switch (this) {
            case RATING:
                Attributes attrs = Attributes.of(helper);
                LocalizerType localizer = helper.localizer();
                String localized = localizer.localize("popup_title_notNow");
                Attribute attribute = attrs.containsText(localized);
                return XPath.builder().addAttribute(attribute).build();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param platform {@link PlatformType} instance.
     * @return {@link Boolean} value.
     * @see PopupType#applicableTo(PlatformType)
     * @see Platform#IOS
     * @see #RATING
     * @see #NOT_AVAILABLE
     */
    @Override
    public boolean applicableTo(@NotNull PlatformType platform) {
        switch (this) {
            case RATING:
                return platform.equals(Platform.IOS);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
