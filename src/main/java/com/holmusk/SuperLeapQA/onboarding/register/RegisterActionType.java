package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.SignUpMode;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

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
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return A {@link Flowable} instance.
     * @see #rxSignUpButton(SignUpMode)
     */
    @NotNull
    default Flowable<Boolean> rx_register_DoBPicker(@NotNull SignUpMode mode) {
        return rxSignUpButton(mode)
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true);
    }

    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash to register.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_splash_register() {
        return rx_splash_welcome().flatMap(a -> rx_welcome_register());
    }
    //endregion
}
