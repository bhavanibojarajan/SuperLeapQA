package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */
public interface AppDelayType {
    default long generalDelay() {
        return 2000;
    }

    default long splashDelay() {
        return 5000;
    }

    default long splashSwipeScreenDelay() {
        return 5000;
    }
}
