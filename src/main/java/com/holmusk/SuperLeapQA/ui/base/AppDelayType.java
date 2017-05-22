package com.holmusk.SuperLeapQA.ui.base;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.mobile.ios.IOSEngine;

/**
 * Created by haipham on 5/7/17.
 */
public interface AppDelayType {
    /**
     * Catch-all delay.
     * @return {@link Long} value.
     */
    default long generalDelay() {
        return 2000;
    }

    /**
     * Delay for splash screen.
     * @return {@link Long} value.
     */
    default long splashDelay() {
        return 5000;
    }

    /**
     * Delay for unacceptable age input confirm.
     * @return {@link Long} value.
     */
    default long unacceptableAgeInputConfirmDelay() {
        return 5000;
    }

    /**
     * Delay for web view initialization.
     * @return {@link Long} value.
     */
    default long webViewDelay() {
        return 3000;
    }
}
