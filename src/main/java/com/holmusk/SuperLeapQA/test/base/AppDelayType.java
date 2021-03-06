package com.holmusk.SuperLeapQA.test.base;

import com.holmusk.HMUITestKit.test.base.HMDelayType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/7/17.
 */
public interface AppDelayType extends HMDelayType {
    /**
     * Delay between the start of an action and the time the progress bar
     * appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long progressBarWaitDelay(@NotNull Engine<?> engine) {
        return 1000;
    }

    /**
     * Delay between the time the user logs in and that when the progress bar
     * appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long loginProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
    }

    /**
     * Delay between the time the user confirm sign-up information, and when
     * the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long registerProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
    }

    /**
     * Delay between the time the user asks for email confirmation for
     * password retrieval, and the time the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long value}.
     * @see #progressBarWaitDelay(Engine)
     */
    default long forgotPasswordProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
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
     * time the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long invalidAgeInputProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
    }

    /**
     * Delay between the time the user submits valid age inputs, and the
     * time the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long validAgeInputProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
    }

    /**
     * The delay between the time the user submits personal info and the time
     * the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long personalInfoProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
    }

    /**
     * Delay between the time the user submits address info, and the time
     * the progress bar appears.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     * @see #progressBarWaitDelay(Engine)
     */
    default long addressInfoProgressDelay(@NotNull Engine<?> engine) {
        return progressBarWaitDelay(engine);
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
     * Delay for card-adding initialization.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long photoPickerScreenDelay(@NotNull Engine<?> engine) {
        return 3000;
    }

    /**
     * Delay between the time the user changes
     * {@link com.holmusk.HMUITestKit.model.UnitSystem} and the time the
     * effect is in place.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long unitSystemChangeDelay(@NotNull Engine<?> engine) {
        return 1000;
    }

    /**
     * Delay between the time the user deletes a meal, and that when the
     * action succeeds.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long contentDeleteProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Delay between the time the user searches for a query and that when
     * the results (if any) appear.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long searchProgressDelay(@NotNull Engine<?> engine) {
        return 2000;
    }

    /**
     * Get the duration interval for popup polling.
     * @param engine {@link Engine} instance.
     * @return {@link Long} value.
     */
    default long popupPollDuration(@NotNull Engine<?> engine) {
        return 500;
    }
}
