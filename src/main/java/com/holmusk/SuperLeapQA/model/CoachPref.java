package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizationFormat;
import org.swiften.xtestkit.base.element.property.type.base.AttributeType;

/**
 * Created by haipham on 5/12/17.
 */
public enum CoachPref implements AttributeType<String>, InputType {
    MALE,
    FEMALE,
    NO_PREFERENCE;

    @NotNull
    @Override
    public String value() {
        switch (this) {
            case MALE:
                return "user_title_coachPref_male";

            case FEMALE:
                return "user_title_coachPref_female";

            case NO_PREFERENCE:
                return "user_title_coachPref_noPreference";

            default:
                return "";
        }
    }

    @Override
    public String androidViewId() {
        return "text1";
    }

    /**
     * @return A {@link String} value.
     * @see InputType#emptySignUpInputError(UserMode)
     */
    @NotNull
    @Override
    public LocalizationFormat emptySignUpInputError(@NotNull UserMode mode) {
        return LocalizationFormat.builder()
            .withPattern("register_error_coachPrefNotSet")
            .build();
    }
}
