package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLTextInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
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
public interface UnacceptableAgeActionType extends
    UnacceptableAgeValidationType, DOBPickerActionType
{
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
}
