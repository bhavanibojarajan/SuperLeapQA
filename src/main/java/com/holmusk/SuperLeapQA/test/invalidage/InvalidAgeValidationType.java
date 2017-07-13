package com.holmusk.SuperLeapQA.test.invalidage;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerValidationType;
import org.swiften.javautilities.object.HPObjects;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 17/5/17.
 */
public interface InvalidAgeValidationType extends DOBPickerValidationType {
    /**
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see DOBPickerValidationType#rxv_validAgeScreen(Engine)
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_invalidAgeScreen(@NotNull final Engine<?> ENGINE,
                                             @NotNull final UserMode MODE) {
        return Flowable
            .mergeArray(
                ENGINE.rxe_containsText("register_title_weAreOnlyAccepting"),
                ENGINE.rxe_containsText(MODE.validAgeCategoryRangeString()),
                rxe_editField(ENGINE, TextInput.NAME),
                rxe_editField(ENGINE, TextInput.PHONE),
                rxe_editField(ENGINE, TextInput.EMAIL)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Get the confirm button for the unacceptable age inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_invalidAgeSubmit(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_submit")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the continue button after the unacceptable age input is confirmed.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_invalidAgeOk(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_ok")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate the confirmation screen after unacceptable age input is
     * submitted.
     * @return {@link Flowable} instance.
     * @see #rxe_invalidAgeOk(Engine)
     * @see Engine#rxe_containsText(String...)
     * @see HPObjects#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_invalidAgeInputConfirmed(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .mergeArray(
                rxe_invalidAgeOk(ENGINE),
                ENGINE.rxe_containsText("register_title_thanksForInterest"),
                ENGINE.rxe_containsText("register_title_notifyOnLaunch")
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }
}
