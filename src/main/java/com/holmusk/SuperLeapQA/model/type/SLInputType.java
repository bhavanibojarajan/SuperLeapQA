package com.holmusk.SuperLeapQA.model.type;

import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.model.InputType;

/**
 * Created by haipham on 18/5/17.
 */

/**
 * {@link InputType} for Superleap.
 */
public interface SLInputType extends InputType {
    /**
     * Get the relevant empty input error messages for sign up, where applicable.
     * Some {@link InputType} may not have a standard error message, however.
     * (e.g. {@link SLNumericChoiceInputType}.
     * where min/max values are taken into account).
     * @return {@link LCFormat} value.
     */
    @NotNull
    LCFormat emptyInputError(@NotNull UserMode mode);
}
