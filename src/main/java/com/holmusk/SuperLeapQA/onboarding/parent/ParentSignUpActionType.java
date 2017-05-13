package com.holmusk.SuperLeapQA.onboarding.parent;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.onboarding.common.BaseSignUpActionType;
import com.holmusk.SuperLeapQA.onboarding.register.RegisterActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface ParentSignUpActionType extends
    BaseActionType,
    RegisterActionType,
    BaseSignUpActionType,
    ParentSignUpValidationType
{
    /**
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return A {@link Flowable} instance.
     * @see #rxParentSignUpButton()
     */
    @NotNull
    default Flowable<Boolean> rx_register_parentDoBPicker() {
        return rxParentSignUpButton()
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Bridge method that helps navigate from splash screen to parent sign up.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_register()
     * @see #rx_register_parentDoBPicker()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_parentDoBPicker() {
        return rx_splash_register()
            .concatWith(rx_register_parentDoBPicker())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method that helps navigate from splash screen to unacceptable
     * age input.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_parentDoBPicker()
     * @see #rx_DoBPicker_unacceptableAgeInput()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_unacceptableAgeInput() {
        return rx_splash_parentDoBPicker()
            .concatWith(rx_DoBPicker_unacceptableAgeInput())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method that helps navigate from splash screen to acceptable
     * age input.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_parentDoBPicker()
     * @see #rx_DoBPicker_acceptableAgeInput()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_acceptableAgeInput() {
        return rx_splash_parentDoBPicker()
            .concatWith(rx_DoBPicker_acceptableAgeInput())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
}
