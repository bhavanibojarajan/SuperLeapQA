package com.holmusk.SuperLeapQA.onboarding.splash;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */

/**
 * Splash screen navigation.
 */
public interface OnboardingInteractionType extends BaseInteractionType {
    /**
     * Wait for splash screen to finish.
     * @return A {@link Flowable} instance.
     */
    default Flowable<Boolean> rx_splash_wait() {
        long delay = splashDelay();
        return Flowable.timer(delay, TimeUnit.MILLISECONDS).map(a -> true);
    }
}
