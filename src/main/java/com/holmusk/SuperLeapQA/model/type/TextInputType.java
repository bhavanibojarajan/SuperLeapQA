package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/13/17.
 */

import org.jetbrains.annotations.NotNull;

/**
 * This interface provides convenience methods for testing text-based inputs.
 */
public interface TextInputType extends InputType {
    /**
     * Get a random {@link String} input.
     * @return A {@link String} value.
     */
    @NotNull
    String randomInput();
}