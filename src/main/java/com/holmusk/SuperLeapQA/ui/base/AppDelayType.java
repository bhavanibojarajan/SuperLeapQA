package com.holmusk.SuperLeapQA.ui.base;

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
    default long invalidAgeInputConfirmDelay() {
        return 5000;
    }

    /**
     * Delay for web view initialization.
     * @return {@link Long} value.
     */
    default long webViewDelay() {
        return 3000;
    }

    /**
     * Duration for {@link com.holmusk.SuperLeapQA.model.DashboardMode}
     * switcher swipe.
     * @return {@link Integer} value.
     */
    default int dashboardModeSwitcherDuration() {
        return 0;
    }

    /**
     * Delay for card-adding initialization.
     * @return {@link Long} value.
     */
    default long photoPickerScreenDelay() {
        return 3000;
    }

    /**
     * Delay between the time the user logs in and that when the dashboard
     * tutorial screen appears.
     * @return {@link Long} value.
     */
    default long loginProgressDelay() {
        return 5000;
    }

    /**
     * Delay between the time the user submits a meal, and that when the
     * meal page appears.
     * @return {@link Long} value.
     */
    default long mealLogProgressDelay() {
        return 2000;
    }

    /**
     * Delay between the time the user deletes a meal, and that when the
     * action succeeds.
     * @return {@link Long} value.
     */
    default long mealDeleteProgressDelay() {
        return 2000;
    }
}
