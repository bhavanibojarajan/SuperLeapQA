package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/19/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;

/**
 * {@link org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType}
 * for Superleap.
 */
public interface SLChoiceInputType extends ChoiceInputType, SLInputType {
    /**
     * Get the target choice item {@link XPath} instance. This item should be
     * displaying the input text we are interested in.
     * @param platform {@link PlatformType} instance.
     * @param selected {@link String} value of the selected choice.
     * @return {@link XPath} instance.
     */
    @NotNull
    XPath targetChoiceItemXPath(@NotNull PlatformType platform,
                                @NotNull String selected);

    /**
     * Convert {@link String} value into a numeric value.
     * @param value {@link String} value.
     * @return {@link Integer} value.
     */
    default int numericValue(@NotNull String value) {
        return Integer.valueOf(value);
    }

    /**
     * Get the numeric value's {@link String} representation.
     * @param value {@link Integer} value.
     * @return {@link String} value.
     */
    @NotNull
    default String stringValue(int value) {
        return String.valueOf(value);
    }
}
