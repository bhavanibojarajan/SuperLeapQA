package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.signup.main.SignUpActionType;
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
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to sign up.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_register()
     * @see #rx_register_DoBPicker(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_DoBPicker(@NotNull UserMode mode) {
        final RegisterModeActionType THIS = this;
        return rx_splash_register().flatMap(a -> THIS.rx_register_DoBPicker(mode));
    }
    //endregion

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
}
