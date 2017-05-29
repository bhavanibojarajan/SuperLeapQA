package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.action.general.Unidirection;
import org.swiften.xtestkit.base.type.BaseErrorType;

/**
 * Created by haipham on 29/5/17.
 */
public enum DashboardMode implements BaseErrorType {
    BMI,
    ACTIVITY;

    /**
     * Get the swipe {@link Unidirection} with which we can perform a swipe
     * to reveal the current {@link DashboardMode}.
     * @return {@link Unidirection} instance.
     * @see Unidirection#LEFT_RIGHT
     * @see Unidirection#RIGHT_LEFT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public Unidirection swipeDirection() {
        switch (this) {
            case BMI:
                return Unidirection.LEFT_RIGHT;

            case ACTIVITY:
                return Unidirection.RIGHT_LEFT;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
