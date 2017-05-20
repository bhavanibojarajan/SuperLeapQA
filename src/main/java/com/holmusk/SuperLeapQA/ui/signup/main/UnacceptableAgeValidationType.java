package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import org.swiften.javautilities.object.ObjectUtil;
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
     * @see BaseEngine#rx_elementsContainingText(String...)
     * @see #rx_editFieldForInput(SLInputType)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUnacceptableAgeScreen(@NotNull final UserMode MODE) {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .mergeArray(
                ENGINE.rx_elementsContainingText("register_title_weAreOnlyAccepting"),
                ENGINE.rx_elementsContainingText(MODE.acceptableAgeCategoryRangeString()),
                ENGINE.rx_elementsContainingText("+65"),
                rx_editFieldForInput(TextInput.NAME),
                rx_editFieldForInput(TextInput.PHONE),
                rx_editFieldForInput(TextInput.EMAIL)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Get the confirm button for the unacceptable age inputs.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementsContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeSubmitButton() {
        return engine()
            .rx_elementsContainingText("register_title_submit")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the continue button after the unacceptable age input is confirmed.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementsContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeInputOkButton() {
        return engine()
            .rx_elementsContainingText("register_title_ok")
            .firstElement()
            .toFlowable();
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
     * @see BaseEngine#rx_elementsContainingText(String...)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUnacceptableAgeInputConfirmation() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .mergeArray(
                rxUnacceptableAgeInputOkButton(),
                ENGINE.rx_elementsContainingText("register_title_thanksForInterest"),
                ENGINE.rx_elementsContainingText("register_title_notifyOnLaunch")
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
