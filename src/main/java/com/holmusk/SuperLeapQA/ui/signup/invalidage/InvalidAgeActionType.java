package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface InvalidAgeActionType extends InvalidAgeValidationType, DOBPickerActionType {
    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterRandomInput(Engine, SLTextInputType)
     * @see #invalidAgeInputConfirmDelay()
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_enterInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        final InvalidAgeActionType THIS = this;

        return rxa_enterRandomInput(ENGINE, TextInput.NAME)
            .flatMap(a -> THIS.rxa_enterRandomInput(ENGINE, TextInput.EMAIL))
            .flatMap(a -> THIS.rxa_enterRandomInput(ENGINE, TextInput.PHONE));
    }

    /**
     * Confirm email subscription for future program expansion.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_invalidAgeSubmit(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rx_e_invalidAgeSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter and confirm invalid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterInvalidAgeInputs(Engine)
     * @see #rx_a_confirmInvalidAgeInputs(Engine)
     * @see #invalidAgeInputConfirmDelay()
     */
    @NotNull
    default Flowable<?> rxa_enterAndConfirmInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        final InvalidAgeActionType THIS = this;

        return rxa_enterInvalidAgeInputs(ENGINE)
            .flatMap(a -> THIS.rx_a_confirmInvalidAgeInputs(ENGINE))
            .delay(invalidAgeInputConfirmDelay(), TimeUnit.MILLISECONDS);

    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_invalidAgeOk(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_completeInvalidAgeInput(@NotNull final Engine<?> ENGINE) {
        return rx_e_invalidAgeOk(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
