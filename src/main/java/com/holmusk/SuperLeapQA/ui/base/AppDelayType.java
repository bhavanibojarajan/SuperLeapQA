package com.holmusk.SuperLeapQA.ui.base;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 5/7/17.
 */
public interface AppDelayType extends BaseErrorType {
    /**
     * Catch-all delay.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #NOT_AVAILABLE
     */
    default long generalDelay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return 1000;
        } else if (engine instanceof IOSEngine) {
            return 2000;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Delay for splash screen.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long splashDelay(@NotNull Engine<?> engine) {
        return 5000;
    }

    /**
     * Delay for unacceptable age input confirm.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long invalidAgeInputConfirmDelay(@NotNull Engine<?> engine) {
        return 5000;
    }

    /**
     * Delay for web view initialization.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long webViewDelay(@NotNull Engine<?> engine) {
        return 3000;
    }

    /**
     * Duration for {@link com.holmusk.SuperLeapQA.model.DashboardMode}
     * switcher swipe.
     * @param engine {@link Engine} instance.
     * @return {@link Integer} value.
     */
    default int dashboardModeSwitcherDuration(@NotNull Engine<?> engine) {
        return 0;
    }

    /**
     * Delay for card-adding initialization.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long photoPickerScreenDelay(@NotNull Engine<?> engine) {
        return 3000;
    }

    /**
     * Delay between the time the user logs in and that when the dashboard
     * tutorial screen appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #NOT_AVAILABLE
     */
    default long loginProgressDelay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return 8000;
        } else if (engine instanceof IOSEngine) {
            return 5000;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Delay between the time the user submits a meal, and that when the
     * meal page appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long mealLogProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Delay between the time the user deletes a meal, and that when the
     * action succeeds.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long mealDeleteProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }
}
