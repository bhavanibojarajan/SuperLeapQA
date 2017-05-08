package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeInteractionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.engine.base.BaseEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterInteractionType extends
    BaseInteractionType,
    WelcomeInteractionType
{
    /**
     * Navigate from welcome to register screen, assuming the user is already
     * in the welcome screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_welcome_register() {
        BaseEngine<?> engine = currentEngine();

        return engine
            .rxElementContainingText("welcome_title_register")
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Bridge method that helps navigate from splash to register.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_splash_register() {
        return rx_splash_welcome().flatMap(a -> rx_welcome_register());
    }
}
