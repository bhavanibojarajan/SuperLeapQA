package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 19/5/17.
 */
public interface InvalidAgeTestHelperType extends InvalidAgeActionType {
    /**
     * Check if either the {@link TextInput#PHONE} or {@link TextInput#EMAIL}
     * input is required, assuming the user is already in the unacceptable age
     * input screen.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT {@link TextInput} instance. Should be either
     * {@link TextInput#EMAIL} or {@link TextInput#PHONE}.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see #rx_a_confirmInvalidAgeInputs(Engine)
     * @see #rx_a_watchProgressBarUntilHidden(Engine)
     * @see #rx_a_completeInvalidAgeInput(Engine)
     */
    @NotNull
    default Flowable<?> rx_h_invalidAgeInputRequired(@NotNull final Engine<?> ENGINE,
                                                     @NotNull final TextInput INPUT) {
        final InvalidAgeActionType THIS = this;

        return rx_a_enterRandomInput(ENGINE, TextInput.NAME)
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, INPUT))
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rx_a_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rx_a_watchProgressBarUntilHidden(ENGINE))
            .flatMap(a -> THIS.rx_v_invalidAgeInputConfirmed(ENGINE))
            .flatMap(a -> THIS.rx_a_completeInvalidAgeInput(ENGINE));
    }

    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see #invalidAgeInputConfirmDelay()
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_enterAndCheckInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        final InvalidAgeActionType THIS = this;
        long delay = invalidAgeInputConfirmDelay();

        return rx_a_enterRandomInput(ENGINE, TextInput.NAME)
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, TextInput.EMAIL))
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, TextInput.PHONE))
            .flatMap(a -> THIS.rx_a_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rx_v_invalidAgeInputConfirmed(ENGINE))
            .delay(delay, TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rx_e_invalidAgeOk(ENGINE))
            .flatMap(ENGINE::rx_click)
            .flatMap(a -> THIS.rx_v_welcomeScreen(ENGINE));
    }
}
