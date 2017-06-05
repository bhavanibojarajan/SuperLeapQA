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
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String cardSelectorText() {
        switch (this) {
            case MEAL:
                return "addCard_title_food";

            case WEIGHT:
                return "addCard_title_weight";

            case ACTIVITY:
                return "addCard_title_activity";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the view id for each FAB button for
     * {@link Platform#ANDROID}.
     * @return {@link String} value.
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case MEAL:
                return "fab_food";

            case WEIGHT:
                return "fab_weight";

            case ACTIVITY:
                return "fab_activity";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
