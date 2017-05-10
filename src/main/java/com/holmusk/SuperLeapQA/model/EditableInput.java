package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum EditableInput {
    NAME,
    EMAIL,
    PHONE,
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREFERENCE;

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case NAME:
                return "et_name";

            case EMAIL:
                return "et_email";

            case PHONE:
                return "et_phone";

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
}
