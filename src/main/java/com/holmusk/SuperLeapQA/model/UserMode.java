package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.InputType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum UserMode {
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

    /**
     * Get the personal information inputs for this {@link UserMode}.
     * @return A {@link List} of {@link InputType}.
     */
    @NotNull
    public List<InputType> personalInformation() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.CHILD_NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.HOME
                );

            default:
                return Collections.emptyList();
        }
    }

    /**
     * Get the minimum acceptable age for the current {@link UserMode}.
     * @return An {@link Integer} value.
     */
    public int minAcceptableAge() {
        switch (this) {
            case PARENT:
                return 5;

            case TEEN:
                return 16;

            default:
                return 0;
        }
    }

    /**
     * Get the maximum acceptable age for the current {@link UserMode}.
     * @return An {@link Integer} value.
     */
    public int maxAcceptableAge() {
        switch (this) {
            case PARENT:
                return 6;

            case TEEN:
                return 19;

            default:
                return 0;
        }
    }
}
