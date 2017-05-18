package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements BaseErrorType, SLInputType, AndroidChoiceInputType {
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREFERENCE;

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     * @see AndroidInputType#androidViewId()
     */
    @NotNull
    @Override
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
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the scroll view picker id for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @return A {@link String} value.
     */
    @NotNull
    @Override
    public String androidScrollViewPickerId() {
        switch (this) {
            case ETHNICITY:
            case COACH_PREFERENCE:
                return "select_dialog_listview";

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
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

        switch (this) {
            case COACH_PREFERENCE:
                error = "register_error_coachPrefNotSet";
                break;

            case ETHNICITY:
                error = "register_error_ethnicityNotSet";
                break;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }

        return LCFormat.builder().withPattern(error).build();
    }
}
