package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkitcomponents.direction.Direction;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;

/**
 * Created by haipham on 29/5/17.
 */
public enum DashboardMode implements ErrorProviderType {
    BMI,
    ACTIVITY;

    /**
     * Get the swipe {@link Direction} with which we can perform a swipe
     * to reveal the current {@link DashboardMode}.
     * @return {@link Direction} instance.
     */
    @NotNull
    public Direction swipeDirection() {
        switch (this) {
            case BMI:
                return Direction.LEFT_RIGHT;

            case ACTIVITY:
                return Direction.RIGHT_LEFT;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
