package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender {
    MALE,
    FEMALE;

    /**
     * Get the localizable title for the current {@link Gender}.
     * @return A {@link String} value.
     */
    @NotNull
    public String localizable() {
        switch (this) {
            case MALE:
                return "user_title_gender_male";

            case FEMALE:
                return "user_title_gender_female";

            default:
                return "";
        }
    }
}
