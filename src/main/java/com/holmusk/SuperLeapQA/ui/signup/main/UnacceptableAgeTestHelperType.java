package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLTextInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 19/5/17.
 */
public interface UnacceptableAgeTestHelperType extends UnacceptableAgeActionType {
    /**
     * Check if either the {@link TextInput#PHONE} or {@link TextInput#EMAIL}
     * input is required, assuming the user is already in the unacceptable age
     * input screen.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT A {@link TextInput} instance. Should be either
     * {@link TextInput#EMAIL} or {@link TextInput#PHONE}.
     * @return A {@link Flowable} instance.
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see #rx_a_confirmUnacceptableAgeInput(Engine)
     * @see #rx_a_watchProgressBarUntilHidden(Engine)
     * @see #rx_v_unacceptableAgeInputCompleted(Engine)
     */
    @NotNull
    default Flowable<?> rx_h_unacceptableAgeInputRequired(@NotNull final Engine<?> ENGINE,
                                                          @NotNull final TextInput INPUT) {
        final UnacceptableAgeActionType THIS = this;

        return rx_a_enterRandomInput(ENGINE, TextInput.NAME)
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, INPUT))
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rx_a_confirmUnacceptableAgeInput(ENGINE))
            .flatMap(a -> THIS.rx_a_watchProgressBarUntilHidden(ENGINE))
            .flatMap(a -> THIS.rx_v_unacceptableAgeInputConfirmed(ENGINE))
            .flatMap(a -> THIS.rx_v_unacceptableAgeInputCompleted(ENGINE));
    }

    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @param ENGINE {@link Engine} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see #unacceptableAgeInputConfirmDelay()
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_enterAndCheckUnacceptableAgeInputs(@NotNull final Engine<?> ENGINE) {
        final UnacceptableAgeActionType THIS = this;
        long delay = unacceptableAgeInputConfirmDelay();

        return rx_a_enterRandomInput(ENGINE, TextInput.NAME)
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, TextInput.EMAIL))
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, TextInput.PHONE))
            .flatMap(a -> THIS.rx_a_confirmUnacceptableAgeInput(ENGINE))
            .flatMap(a -> THIS.rx_v_unacceptableAgeInputConfirmed(ENGINE))
            .delay(delay, TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rx_e_unacceptableAgeOk(ENGINE))
            .flatMap(ENGINE::rx_click)
            .flatMap(a -> THIS.rxValidateWelcomeScreen(ENGINE));
    }
}
