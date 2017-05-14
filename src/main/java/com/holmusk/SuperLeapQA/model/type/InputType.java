package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/13/17.
 */

import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizationFormat;

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
     * (e.g. {@link com.holmusk.SuperLeapQA.model.type.NumericSelectableInputType}.
     * where min/max values are taken into account).
     * @return A {@link LocalizationFormat} value.
     */
    @NotNull
    LocalizationFormat emptySignUpInputError(@NotNull UserMode mode);
}
