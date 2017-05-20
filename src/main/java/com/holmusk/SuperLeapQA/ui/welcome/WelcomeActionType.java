package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    /**
     * Navigate from welcome to register screen, assuming the user is already
     * on the welcome screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_welcomeRegister(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_welcome_register(@NotNull final Engine<?> ENGINE) {
        return rx_e_welcomeRegister(ENGINE)
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Wait for splash screen to finish and navigate to welcome screen.
     * @return {@link Flowable} instance.
     * @see #splashDelay()
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_splash_welcome() {
        long delay = splashDelay();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        return Flowable.timer(delay, unit);
    }
}
