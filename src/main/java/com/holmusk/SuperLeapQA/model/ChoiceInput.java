package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.InputType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/13/17.
 */
public enum  ChoiceInput implements InputType {
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
}
