package com.holmusk.SuperLeapQA.onboarding.welcome;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    /**
     * Wait for splash screen to finish and navigate to welcome screen.
     * @return A {@link Flowable} instance.
     */
    default Flowable<Boolean> rx_splash_welcome() {
        long delay = splashDelay();
        return Flowable.timer(delay, TimeUnit.MILLISECONDS).map(a -> true);
    }
}
