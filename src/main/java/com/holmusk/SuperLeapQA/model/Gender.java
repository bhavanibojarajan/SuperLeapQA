package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizationFormat;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender implements InputType {
    MALE,
    FEMALE;

    /**
     * @return A {@link String} value.
     * @see InputType#androidViewId()
     */
    @Override
    public String androidViewId() {
        switch (this) {
            case MALE:
                return "btn_male";

            case FEMALE:
                return "btn_female";

            default:
                return "";
        }
    }

    /**
     * @return A {@link String} value.
     * @see InputType#emptySignUpInputError(UserMode)
     */
    @NotNull
    @Override
    public LocalizationFormat emptySignUpInputError(@NotNull UserMode mode) {
        String error;

        switch (mode) {
            case PARENT:
                error = "parentSignUp_error_genderNotSelected";
                break;

            case TEEN:
                error = "teenSignUp_error_genderNotSelected";
                break;

            default:
                error = "";
                break;
        }

        return LocalizationFormat.builder().withPattern(error).build();
    }
}
