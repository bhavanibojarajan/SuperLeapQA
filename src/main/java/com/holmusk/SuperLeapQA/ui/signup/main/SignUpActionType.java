package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidTextInputType;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpActionType extends
    BaseActionType,
    SignUpValidationType,
    RegisterModeActionType,
    WelcomeActionType
{
    /**
     * Enter an input for a {@link TextInput}.
     * @param input A {@link TextInputType} instance.
     * @param TEXT A {@link String} value.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(AndroidInputType)
     * @see BaseEngine#rxSendKey(WebElement, String...)
     */
    @NotNull
    default <P extends AndroidInputType>
    Flowable<WebElement> rxEnterInput(@NotNull P input, @NotNull final String TEXT) {
        final BaseEngine<?> ENGINE = engine();
        return rxEditFieldForInput(input).flatMap(a -> ENGINE.rxSendKey(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param input A {@link TextInputType} instance.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see #rxEnterInput(AndroidInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default <P extends AndroidTextInputType> Flowable<WebElement>
    rxEnterRandomInput(@NotNull P input) {
        return rxEnterInput(input, input.randomInput());
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input A {@link InputType} instance.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(AndroidInputType) )
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default <P extends AndroidInputType>
    Flowable<Boolean> rxClickInputField(@NotNull P input) {
        return rxEditFieldForInput(input).flatMap(engine()::rxClick).map(BooleanUtil::toTrue);
    }
}
