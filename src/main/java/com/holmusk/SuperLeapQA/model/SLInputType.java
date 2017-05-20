package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 18/5/17.
 */

/**
 * {@link InputType} for Superleap.
 */
public interface SLInputType extends AndroidInputType {
    /**
     * Get the relevant empty input error messages for sign up, where applicable.
     * Some {@link InputType} may not have a standard error message, however.
     * (e.g. {@link SLNumericInputType}.
     * where min/max values are taken into account).
     * @return {@link LCFormat} value.
     */
    @NotNull
    LCFormat emptyInputError(@NotNull UserMode mode);
}
