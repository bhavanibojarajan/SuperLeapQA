package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/13/17.
 */
public enum SignUpMode {
    PARENT,
    TEEN;

    /**
     * Get the sign up button's id for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @return A {@link String} value.
     */
    @NotNull
    public String androidSignUpButtonId() {
        switch (this) {
            case PARENT:
                return "btnRegChild";

            case TEEN:
                return "btnRegSelf";

            default:
                return "";
        }
    }
}
