package com.holmusk.SuperLeapQA.ui.signup.main;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 17/5/17.
 */
public interface UnacceptableAgeActionType extends
    UnacceptableAgeValidationType, DOBPickerActionType
{
    /**
     * Confirm email subscription for future program expansion.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxUnacceptableAgeSubmitButton()
     * @see BaseEngine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmUnacceptableAgeInput() {
        final BaseEngine<?> ENGINE = engine();

        return rxUnacceptableAgeSubmitButton()
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }
}
