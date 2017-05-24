package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

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
}
