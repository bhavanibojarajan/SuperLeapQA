package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 29/5/17.
 */
public enum CardType implements BaseErrorType {
    MEAL,
    WEIGHT,
    ACTIVITY;

    /**
     * Get the displayed text on a {@link CardType} selector button.
     * @return {@link String} value.
     * @see #ACTIVITY
     * @see #MEAL
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case ACTIVITY:
                return "addCard_title_activity";

            case MEAL:
                return "addCard_title_food";

            case WEIGHT:
                return "addCard_title_weight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the view id for each FAB button for {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see Platform#ANDROID
     * @see #ACTIVITY
     * @see #MEAL
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case ACTIVITY:
                return "fab_activity";

            case MEAL:
                return "fab_food";

            case WEIGHT:
                return "fab_weight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
