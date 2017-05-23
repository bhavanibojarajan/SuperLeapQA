package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/23/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.property.type.base.AttributeType;

/**
 * This interface provides methods to work with {@link SLTextChoiceInputType}.
 * Basically, each {@link SLTextChoiceInputType} should have a
 * {@link java.util.List} of {@link SLTextChoiceInputItemType}.
 */
public interface SLTextChoiceInputItemType {
    /**
     * Use this {@link String} to locale a {@link SLTextChoiceInputItemType}
     * instance. This can be helpful when we are calling
     * {@link SLTextChoiceInputType#numericValue(String)}.
     * @return {@link String} value.
     */
    @NotNull
    String stringValue();
}
