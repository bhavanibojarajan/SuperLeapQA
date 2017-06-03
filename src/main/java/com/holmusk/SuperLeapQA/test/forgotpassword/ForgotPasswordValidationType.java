package com.holmusk.SuperLeapQA.test.forgotpassword;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/26/17.
 */
public interface ForgotPasswordValidationType extends BaseValidationType {
    /**
     * Get the submit button in
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_forgotPasswordSubmit(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText(
                "forgotPassword_title_ok",
                "forgotPassword_title_submit"
            )
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_forgotPasswordSubmit(Engine)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_forgotPassword(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_forgotPasswordSubmit(engine),
                rxe_editField(engine, TextInput.EMAIL)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Get the ok button for the email confirmation dialog.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_emailSentConfirm(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("forgotPassword_title_ok")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the email sent message {@link WebElement} within the confirmation
     * dialog.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_emailSentMessage(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText(
                "forgotPassword_title_emailSent",
                "forgotPassword_title_weWillSendInstructions"
            )
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate the email sent confirmation.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_emailSentConfirm(Engine)
     * @see #rxe_emailSentMessage(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_emailSentConfirmation(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_emailSentConfirm(engine),
                rxe_emailSentMessage(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
