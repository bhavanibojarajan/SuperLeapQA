package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeActionType extends
    BaseActionType,
    WelcomeActionType,
    RegisterModeValidationType
{
    /**
     * Navigate from welcome to register screen, assuming the user is already
     * on the welcome screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_welcome_register() {
        return rxWelcomeRegisterButton()
            .flatMap(engine()::rxClick).map(a -> true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline());
    }

    /**
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return A {@link Flowable} instance.
     * @see #rxSignUpButton(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_register_DoBPicker(@NotNull UserMode mode) {
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
