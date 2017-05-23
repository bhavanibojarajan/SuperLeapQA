package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 17/5/17.
 */
public interface InvalidAgeActionType extends InvalidAgeValidationType, DOBPickerActionType {
    /**
     * Confirm email subscription for future program expansion.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_invalidAgeSubmit(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmInvalidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rx_e_invalidAgeSubmit(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_invalidAgeOk(Engine)
     * @see Engine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_a_completeInvalidAgeInput(@NotNull final Engine<?> ENGINE) {
        return rx_e_invalidAgeOk(ENGINE).flatMap(ENGINE::rx_click);
    }
}
