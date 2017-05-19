package com.holmusk.SuperLeapQA.ui.signup.main;

import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 17/5/17.
 */
public interface UnacceptableAgeValidationType extends DOBPickerValidationType {
    /**
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see DOBPickerValidationType#rxValidateUnacceptableAgeScreen(UserMode)
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see #rxEditFieldForInput(InputType)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUnacceptableAgeScreen(@NotNull final UserMode MODE) {
        final BaseEngine<?> ENGINE = engine();

        return ENGINE.rx_elementContainingText("register_title_weAreOnlyAccepting")
            .flatMap(a -> ENGINE.rx_elementContainingText(MODE.acceptableAgeCategoryRangeString()))
            .flatMap(a -> ENGINE.rx_elementContainingText("+65"))
            .flatMap(a -> rx_editFieldForInput(TextInput.NAME))
            .flatMap(a -> rx_editFieldForInput(TextInput.PHONE))
            .flatMap(a -> rx_editFieldForInput(TextInput.EMAIL))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for the unacceptable age inputs.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeSubmitButton() {
        return engine().rx_elementContainingText("register_title_submit");
    }

    /**
     * Get the continue button after the unacceptable age input is confirmed.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeInputOkButton() {
        return engine().rx_elementContainingText("register_title_ok");
    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxUnacceptableAgeInputOkButton()
     * @see BaseEngine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmUnacceptableAgeInputCompleted() {
        final BaseEngine<?> ENGINE = engine();

        return rxUnacceptableAgeInputOkButton()
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Validate the confirmation screen after unacceptable age input is
     * submitted.
     * @return A {@link Flowable} instance.
     * @see #rxUnacceptableAgeInputOkButton()
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxValidateUnacceptableAgeInputConfirmation() {
        final BaseEngine<?> ENGINE = engine();

        return rxUnacceptableAgeInputOkButton()
            .flatMap(a -> ENGINE.rx_elementContainingText("register_title_thanksForInterest"))
            .flatMap(a -> ENGINE.rx_elementContainingText("register_title_notifyOnLaunch"))
            .map(BooleanUtil::toTrue);
    }
}
