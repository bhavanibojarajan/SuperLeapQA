package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpActionType extends SignUpValidationType, RegisterModeActionType {
    /**
     * Enter an input for {@link TextInput}.
     * @param input {@link TextInputType} instance.
     * @param TEXT {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see Engine#rxSendKey(WebElement, String...)
     */
    @NotNull
    default <P extends SLInputType> Flowable<WebElement>
    rx_a_enterInput(@NotNull final Engine<?> ENGINE,
                    @NotNull P input,
                    @NotNull final String TEXT) {
        return rx_e_editField(ENGINE, input).flatMap(a -> ENGINE.rxSendKey(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param engine {@link Engine} instance.
     * @param input {@link TextInputType} instance.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterInput(Engine, SLInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default <P extends SLTextInputType> Flowable<WebElement>
    rx_a_enterRandomInput(@NotNull Engine<?> engine, @NotNull P input) {
        return rx_a_enterInput(engine, input, input.randomInput());
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input {@link InputType} instance.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)   )
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default <P extends SLInputType> Flowable<?>
    rx_a_clickInputField(@NotNull final Engine<?> ENGINE, @NotNull P input) {
        return rx_e_editField(ENGINE, input).flatMap(ENGINE::rx_click);
    }
}
