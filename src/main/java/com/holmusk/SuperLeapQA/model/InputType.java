package com.holmusk.SuperLeapQA.model;

/**
 * Created by haipham on 5/13/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;

/**
 * This interface provides methods to help locate
 * {@link org.openqa.selenium.WebElement} that corresponds to each
 * {@link InputType} instance.
 */
public interface InputType {
    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     */
    @NotNull
    String androidViewId();

    /**
     * Get the relevant empty input error messages for sign up, where applicable.
     * Some {@link InputType} may not have a standard error message, however.
     * (e.g. {@link NumericSelectableInputType}.
     * where min/max values are taken into account).
     * @return A {@link LCFormat} value.
     */
    @NotNull
    LCFormat emptyInputError(@NotNull UserMode mode);
}