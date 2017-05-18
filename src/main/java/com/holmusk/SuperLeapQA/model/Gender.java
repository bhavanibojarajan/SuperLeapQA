package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender implements AndroidInputType, SLInputType {
    MALE,
    FEMALE;

    /**
     * @return A {@link String} value.
     * @see AndroidInputType#androidViewId()
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
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        String error;

        switch (mode) {
            case PARENT:
                error = "parentSignUp_error_genderNotSelected";
                break;

            case TEEN_UNDER_18:
                error = "teenSignUp_error_genderNotSelected";
                break;

            default:
                error = "";
                break;
        }

        return LCFormat.builder().withPattern(error).build();
    }
}
