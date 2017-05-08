package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface ParentSignUpInteractionType extends
    BaseInteractionType,
    RegisterInteractionType,
    CommonSignUpInteractionType,
    ParentSignUpValidationType
{
    /**
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_register_parentSignUp() {
        return rxParentSignUpButton()
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Bridge method that helps navigate from register to parent sign up.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rx_splash_parentSignUp() {
        return rx_splash_register().flatMap(a -> rx_register_parentSignUp());
    }
}
