package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.action.input.type.NumericSelectableType;

/**
 * Created by haipham on 18/5/17.
 */

/**
 * {@link NumericSelectableType} for Superleap.
 */
public interface SLNumericSelectableType extends SLInputType, NumericSelectableType {
    /**
     * We provide a default implementation to make use of max and min values.
     * @param mode A {@link UserMode} instance.
     * @return A {@link org.swiften.javautilities.localizer.LCFormat} value.
     * @see SLInputType#emptyInputError(UserMode)
     * @see #emptyInputErrorFormat()
     * @see #minSelectableNumericValue()
     * @see #maxSelectableNumericValue()
     * @see #stringValue(double)
     */
    @NotNull
    @Override
    default LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.builder()
            .withPattern(emptyInputErrorFormat())
            .addArgument(stringValue(minSelectableNumericValue()))
            .addArgument(stringValue(maxSelectableNumericValue()))
            .build();
    }
}
