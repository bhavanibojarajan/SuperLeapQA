package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash to register.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_welcome()
     * @see #rx_welcome_register()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_register() {
        final WelcomeActionType THIS = this;
        return rx_splash_welcome().flatMap(a -> THIS.rx_welcome_register());
    }
    //endregion

    /**
     * Navigate from welcome to register screen, assuming the user is already
     * on the welcome screen.
     * @return A {@link Flowable} instance.
     * @see #rxWelcomeRegisterButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_welcome_register() {
        final BaseEngine<?> ENGINE = engine();

        return rxWelcomeRegisterButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Wait for splash screen to finish and navigate to welcome screen.
     * @return A {@link Flowable} instance.
     * @see #splashDelay()
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_welcome() {
        long delay = splashDelay();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        Scheduler scheduler = Schedulers.trampoline();
        return Flowable.timer(delay, unit, scheduler).map(BooleanUtil::toTrue);
    }
}
