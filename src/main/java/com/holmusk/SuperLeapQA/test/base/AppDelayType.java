package com.holmusk.SuperLeapQA.test.base;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.common.BaseErrorType;

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
        } else {
            return 2000;
        }
    }

    /**
     * Delay between the time the user logs in and that when the dashboard
     * tutorial screen appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long loginProgressDelay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return 10000;
        } else {
            return 5000;
        }
    }

    /**
     * Delay between the time the user confirm sign-up information, and the
     * time {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW}
     * appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long registerProgressDelay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return 10000;
        } else {
            return 5000;
        }
    }

    /**
     * Delay between the time the user asks for email confirmation for
     * password retrieval, and the time the email sent notification is shown.
     * @param engine {@link Engine} instance.
     * @return {@link Long value}.
     */
    default long forgotPasswordProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Delay for splash screen.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long splashDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Delay between the time the user submits invalid age inputs, and the
     * time the confirmation screen is shown.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long invalidAgeInputProgressDelay(@NotNull Engine<?> engine) {
        return 3000;
    }

    /**
     * Delay between the time the user submits valid age inputs, and the
     * time the user is directed to the next page.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long validAgeInputProgressDelay(@NotNull Engine<?> engine) {
        return 3000;
    }

    /**
     * The delay between the time the user submits personal info and the time
     * when the app navigates to the next page.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long personalInfoProgressDelay(@NotNull Engine<?> engine) {
        return 0;
    }

    /**
     * Delay between the time the user submits address info, and the time
     * the app redirects to the next page.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long addressInfoProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
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

    /**
     * Delay between the time the user searches for a query and that when
     * the results (if any) appear.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long mealSearchProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Delay between the time the user clicks submit weight and that when
     * the next screen appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long weightLogProgressDelay(@NotNull Engine<?> engine) {
        return 500;
    }
}
