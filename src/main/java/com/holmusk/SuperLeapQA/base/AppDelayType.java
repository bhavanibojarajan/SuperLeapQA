package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */
public interface AppDelayType {
    default long splashDelay() {
        return 5000;
    }
}
