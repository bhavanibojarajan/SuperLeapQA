package com.holmusk.SuperLeapQA.ui.forgotpassword;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLTextType;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.ui.login.LoginActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/26/17.
 */
public interface ForgotPasswordActionType extends
    BaseActionType,
    ForgotPasswordValidationType,
    LoginActionType
{
    /**
     * Enter forgot password inputs. Should only take {@link TextInput#EMAIL}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see TextInput#EMAIL
     * @see #rxa_randomInputs(Engine, SLTextType...)
     */
    @NotNull
    default Flowable<?> rxa_enterPassRecoveryInputs(@NotNull Engine<?> engine) {
        return rxa_randomInputs(engine, TextInput.EMAIL);
    }

    /**
     * Confirm forgot password inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_forgotPasswordSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPassRecovery(@NotNull final Engine<?> ENGINE) {
        return rxe_forgotPasswordSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter and confirm forgot password inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterPassRecoveryInputs(Engine)
     * @see #rxa_confirmPassRecovery(Engine)
     */
    @NotNull
    default Flowable<?> rxa_recoverPassword(@NotNull final Engine<?> ENGINE) {
        final ForgotPasswordActionType THIS = this;

        return rxa_enterPassRecoveryInputs(ENGINE)
            .flatMap(a -> THIS.rxa_confirmPassRecovery(ENGINE));
    }

    /**
     * Dismiss the email sent confirmation dialog.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_emailSentConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmEmailSent(@NotNull final Engine<?> ENGINE) {
        return rxe_emailSentConfirm(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
