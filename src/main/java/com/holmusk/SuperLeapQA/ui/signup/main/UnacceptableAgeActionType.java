package com.holmusk.SuperLeapQA.ui.signup.main;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 17/5/17.
 */
public interface UnacceptableAgeActionType extends UnacceptableAgeValidationType, DOBPickerActionType {
    /**
     * Confirm email subscription for future program expansion.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_unacceptableAgeSubmit(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmUnacceptableAgeInput(@NotNull final Engine<?> ENGINE) {
        return rx_e_unacceptableAgeSubmit(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_unacceptableAgeOk(Engine)
     * @see Engine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_a_completeUnacceptableAgeInput(@NotNull final Engine<?> ENGINE) {
        return rx_e_unacceptableAgeOk(ENGINE).flatMap(ENGINE::rx_click);
    }
}
