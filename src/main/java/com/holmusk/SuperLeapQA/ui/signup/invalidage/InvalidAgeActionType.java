package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface InvalidAgeActionType extends InvalidAgeValidationType, DOBPickerActionType {
    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterRandomInputs(Engine, List)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_enterInvalidAgeInputs(@NotNull Engine<?> engine) {
        return rxa_enterRandomInputs(engine,
            TextInput.NAME,
            TextInput.EMAIL,
            TextInput.PHONE);
    }

    /**
     * Confirm email subscription for future program expansion.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_invalidAgeSubmit(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_confirmInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rxe_invalidAgeSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter and confirm invalid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterInvalidAgeInputs(Engine)
     * @see #rxa_confirmInvalidAgeInputs(Engine)
     * @see #invalidAgeInputConfirmDelay()
     */
    @NotNull
    default Flowable<?> rxa_enterAndConfirmInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        final InvalidAgeActionType THIS = this;

        return rxa_enterInvalidAgeInputs(ENGINE)
            .flatMap(a -> THIS.rxa_confirmInvalidAgeInputs(ENGINE))
            .delay(invalidAgeInputConfirmDelay(), TimeUnit.MILLISECONDS);

    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_invalidAgeOk(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_completeInvalidAgeInput(@NotNull final Engine<?> ENGINE) {
        return rxe_invalidAgeOk(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
