package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import io.reactivex.Flowable;
import org.swiften.javautilities.bool.BooleanUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    /**
     * Wait for splash screen to finish and navigate to welcome screen.
     * @return A {@link Flowable} instance.
     * @see #splashDelay()
     * @see BooleanUtil#toTrue(Object)
     */
    default Flowable<Boolean> rx_splash_welcome() {
        long delay = splashDelay();
        return Flowable.timer(delay, TimeUnit.MILLISECONDS).map(BooleanUtil::toTrue);
    }
}
