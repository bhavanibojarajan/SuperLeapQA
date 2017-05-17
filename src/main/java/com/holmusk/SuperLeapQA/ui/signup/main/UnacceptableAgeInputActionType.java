package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.TextInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface UnacceptableAgeInputActionType extends
    UnacceptableAgeInputValidationType,
    DOBPickerActionType
{
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to unacceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_unacceptableAgeInput(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_unacceptableAgeInput(@NotNull UserMode mode) {
        final UnacceptableAgeInputActionType THIS = this;
        return rx_splash_DoBPicker(mode).flatMap(a -> THIS.rx_DoBPicker_unacceptableAgeInput(mode));
    }
    //endregion

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within {
     * @link #acceptableAgeRange(UserMode)}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_unacceptableAgeInput(@NotNull UserMode mode) {
        int age = mode.maxAcceptableAge() + 1;
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Confirm email subscription for future program expansion.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxUnacceptableAgeSubmitButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmUnacceptableAgeInput() {
        final BaseEngine<?> ENGINE = engine();

        return rxUnacceptableAgeSubmitButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Check if either the {@link TextInput#PHONE} or {@link TextInput#EMAIL}
     * input is required, assuming the user is already in the unacceptable age
     * input screen.
     * @param input A {@link TextInput} instance. Should be either
     * {@link TextInput#EMAIL} or {@link TextInput#PHONE}.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #rxConfirmUnacceptableAgeInput()
     * @see #rxWatchUntilProgressBarNoLongerVisible()
     * @see #rxConfirmUnacceptableAgeInputCompleted()
     */
    @NotNull
    default Flowable<Boolean> rxCheckUnacceptableAgeInputRequired(@NotNull TextInput input) {
        final UnacceptableAgeInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return rxEnterRandomInput(TextInput.NAME)
            .flatMap(a -> THIS.rxEnterRandomInput(input))
            .flatMap(a -> ENGINE.rxHideKeyboard())
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rxWatchUntilProgressBarNoLongerVisible())
            .flatMap(a -> THIS.rxValidateUnacceptableAgeInputConfirmation())
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInputCompleted());
    }

    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #unacceptableAgeInputConfirmDelay()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateUnacceptableAgeInputs() {
        final UnacceptableAgeInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        long delay = unacceptableAgeInputConfirmDelay();

        return rxEnterRandomInput(TextInput.NAME)
            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.EMAIL))
            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.PHONE))
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rxValidateUnacceptableAgeInputConfirmation())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> THIS.rxUnacceptableAgeInputOkButton())
            .flatMap(ENGINE::rxClick).map(a -> true)
            .flatMap(a -> THIS.rxValidateWelcomeScreen());
    }
}