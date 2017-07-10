package com.holmusk.SuperLeapQA.model;

import org.swiften.xtestkitcomponents.common.ErrorProviderType;

/**
 * Created by haipham on 29/6/17.
 */
public enum RewardedAction implements ErrorProviderType {
    LOG_MEAL,
    LOG_WEIGHT;

    /**
     * Get the number of points awarded for each instance of
     * {@link RewardedAction} logged.
     * @return {@link Double} value.
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
