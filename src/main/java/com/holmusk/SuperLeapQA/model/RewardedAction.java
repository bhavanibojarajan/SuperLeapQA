package com.holmusk.SuperLeapQA.model;

import org.swiften.xtestkitcomponents.common.BaseErrorType;

/**
 * Created by haipham on 29/6/17.
 */
public enum RewardedAction implements BaseErrorType {
    LOG_MEAL,
    LOG_WEIGHT;

    /**
     * Get the number of points awarded for each instance of
     * {@link RewardedAction} logged.
     * @return {@link Double} value.
     * @see #LOG_MEAL
     * @see #LOG_WEIGHT
     * @see #NOT_AVAILABLE
     */
    public double points() {
        switch (this) {
            case LOG_MEAL:
                return 1;

            case LOG_WEIGHT:
                return 5;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
