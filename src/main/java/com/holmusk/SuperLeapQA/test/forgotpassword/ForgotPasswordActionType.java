package com.holmusk.SuperLeapQA.test.forgotpassword;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import com.holmusk.SuperLeapQA.test.login.LoginActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

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
     * @see #rxa_randomInputs(Engine, HMTextType...)
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
     * @see #forgotPasswordProgressDelay(Engine)
     * @see #rxe_forgotPasswordSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPassRecovery(@NotNull final Engine<?> ENGINE) {
        return rxe_forgotPasswordSubmit(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(forgotPasswordProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Enter and confirm forgot password inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_enterPassRecoveryInputs(Engine)
     * @see #rxa_confirmPassRecovery(Engine)
     */
    @NotNull
    default Flowable<?> rxa_recoverPassword(@NotNull Engine<?> engine) {
        return Flowable
            .concatArray(
                rxa_enterPassRecoveryInputs(engine),
                rxa_confirmPassRecovery(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Dismiss the email sent confirmation dialog.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxe_emailSentConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmEmailSent(@NotNull final Engine<?> ENGINE) {
        return rxe_emailSentConfirm(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
