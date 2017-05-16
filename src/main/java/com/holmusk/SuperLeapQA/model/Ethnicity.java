package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizationFormat;
import org.swiften.xtestkit.base.element.property.type.base.AttributeType;

/**
 * Created by haipham on 5/12/17.
 */
public enum Ethnicity implements AttributeType<String>, InputType {
    CHINESE,
    MALAY,
    INDIAN,
    JAPANESE,
    ASIAN_OTHER,
    WHITE_NON_HISPANIC,
    AFRICAN,
    LATINO_HISPANIC,
    OTHER;

    @NotNull
    @Override
    public String value() {
        switch (this) {
            case CHINESE:
                return "user_title_ethnicity_chinese";

            case MALAY:
                return "user_title_ethnicity_malay";

            case INDIAN:
                return "user_title_ethnicity_indian";

            case JAPANESE:
                return "user_title_ethnicity_japanese";

            case ASIAN_OTHER:
                return "user_title_ethnicity_asianOther";

            case WHITE_NON_HISPANIC:
                return "user_title_ethnicity_whiteNonHispanic";

            case AFRICAN:
                return "user_title_ethnicity_african";

            case LATINO_HISPANIC:
                return "user_title_ethnicity_latinoHispanic";

            case OTHER:
                return "user_title_ethnicity_other";

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
            .withPattern("register_error_ethnicityNotSet")
            .build();
    }
}
