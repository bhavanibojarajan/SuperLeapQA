package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/13/17.
 */

import org.jetbrains.annotations.NotNull;

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
}
