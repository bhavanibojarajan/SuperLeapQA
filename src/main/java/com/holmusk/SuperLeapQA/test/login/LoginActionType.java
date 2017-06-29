package com.holmusk.SuperLeapQA.test.login;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/26/17.
 */
public interface LoginActionType extends BaseActionType, LoginValidationType {
    /**
     * Navigate to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_forgotPassword(Engine)
     */
    @NotNull
    default Flowable<?> rxa_forgotPasswordFromLogin(@NotNull final Engine<?> ENGINE) {
        return rxe_forgotPassword(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Navigate to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_loginRegister(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_registerFromLogin(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_loginRegister(ENGINE).flatMap(ENGINE::rxa_click);
        } else if (ENGINE instanceof IOSEngine) {
            return rxe_loginRegister(ENGINE)
                .map(ENGINE::middleCoordinate)
                .flatMap(ENGINE::rxa_tap);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Confirm login inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxa_watchUntilHidden(Flowable)
     * @see ObjectUtil#nonNull(Object)
     * @see #loginProgressDelay(Engine)
     * @see #rxe_submit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmLogin(@NotNull final Engine<?> ENGINE) {
        return Flowable.concatArray(
            rxe_submit(ENGINE).flatMap(ENGINE::rxa_click),
            Flowable.timer(loginProgressDelay(ENGINE), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(ENGINE)
        ).all(ObjectUtil::nonNull).toFlowable();
    }

    /**
     * Enter random login credentials.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see TextInput#EMAIL
     * @see TextInput#PASSWORD
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_loginInputs(@NotNull Engine<?> engine) {
        return rxa_randomInputs(engine, TextInput.EMAIL, TextInput.PASSWORD);
    }

    /**
     * Enter and confirm login credentials.
     * @param engine {@link Engine} instance.
     * @param inputs {@link List} of {@link Zip}.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_inputs(Engine, List)
     * @see #rxa_confirmLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_login(@NotNull Engine<?> engine,
                                  @NotNull List<Zip<HMTextType,String>> inputs) {
        return Flowable
            .concatArray(rxa_inputs(engine, inputs), rxa_confirmLogin(engine))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Login with default credentials, as supplied by
     * {@link UserMode#loginCredentials()}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#loginCredentials()
     * @see #rxa_login(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_loginWithDefaults(@NotNull Engine<?> engine,
                                              @NotNull UserMode mode) {
        return rxa_login(engine, mode.loginCredentials());
    }
}
