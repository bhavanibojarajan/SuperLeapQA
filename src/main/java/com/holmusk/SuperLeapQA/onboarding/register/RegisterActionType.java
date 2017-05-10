package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterActionType extends
    BaseActionType,
    WelcomeActionType,
    RegisterValidationType
{
    /**
     * Navigate from welcome to register screen, assuming the user is already
     * on the welcome screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_welcome_register() {
        return rxWelcomeRegisterButton()
            .flatMap(engine()::rxClick)
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
