package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import org.swiften.javautilities.object.ObjectUtil;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 17/5/17.
 */
public interface UnacceptableAgeValidationType extends DOBPickerValidationType {
    /**
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see DOBPickerValidationType#rx_v_acceptableAgeScreen(Engine)
     * @see Engine#rx_containsText(String...)
     * @see #rx_e_editField(Engine, SLInputType)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_unacceptableAgeScreen(@NotNull final Engine<?> ENGINE,
                                                   @NotNull final UserMode MODE) {
        return Flowable
            .mergeArray(
                ENGINE.rx_containsText("register_title_weAreOnlyAccepting"),
                ENGINE.rx_containsText(MODE.acceptableAgeCategoryRangeString()),
                ENGINE.rx_containsText("+65"),
                rx_e_editField(ENGINE, TextInput.NAME),
                rx_e_editField(ENGINE, TextInput.PHONE),
                rx_e_editField(ENGINE, TextInput.EMAIL)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Get the confirm button for the unacceptable age inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_unacceptableAgeSubmit(@NotNull Engine<?> engine) {
        return engine.rx_containsText("register_title_submit").firstElement().toFlowable();
    }

    /**
     * Get the continue button after the unacceptable age input is confirmed.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_unacceptableAgeOk(@NotNull Engine<?> engine) {
        return engine.rx_containsText("register_title_ok").firstElement().toFlowable();
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
    default Flowable<?> rx_v_unacceptableAgeInputCompleted(@NotNull final Engine<?> ENGINE) {
        return rx_e_unacceptableAgeOk(ENGINE)
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Validate the confirmation screen after unacceptable age input is
     * submitted.
     * @return {@link Flowable} instance.
     * @see #rx_e_unacceptableAgeOk(Engine)
     * @see Engine#rx_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_unacceptableAgeInputConfirmed(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .mergeArray(
                rx_e_unacceptableAgeOk(ENGINE),
                ENGINE.rx_containsText("register_title_thanksForInterest"),
                ENGINE.rx_containsText("register_title_notifyOnLaunch")
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
