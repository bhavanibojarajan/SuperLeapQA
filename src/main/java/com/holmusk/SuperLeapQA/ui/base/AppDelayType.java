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
    default long cardAddScreenDelay() {
        return 2000;
    }

    /**
     * Delay for dialog dismissal. This is useful for when there may be
     * multiple permission dialogs that need to be accepted consecutively.
     * @return {@link Long} value.
     */
    default long dialogDismissalDelay() {
        return 2000;
    }
}
