package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.InputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizationFormat;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements InputType {
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREFERENCE;

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     * @see InputType#androidViewId()
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case HEIGHT:
                return "et_height";

            case WEIGHT:
                return "et_weight";

            case ETHNICITY:
                return "et_eth";

            case COACH_PREFERENCE:
                return "et_coachpref";

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

        switch (this) {
            case COACH_PREFERENCE:
                error = "register_error_coachPrefNotSet";
                break;

            case ETHNICITY:
                error = "register_error_ethnicityNotSet";
                break;

            default:
                error = "";
                break;
        }

        return LocalizationFormat.builder().withPattern(error).build();
    }
}