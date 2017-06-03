package com.holmusk.SuperLeapQA.test.login;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.Engine;

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
     */
    @NotNull
    default Flowable<?> rxa_registerFromLogin(@NotNull final Engine<?> ENGINE) {
        return rxe_loginRegister(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Confirm login inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_submit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmLogin(@NotNull final Engine<?> ENGINE) {
        return rxe_submit(ENGINE).flatMap(ENGINE::rxa_click);
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
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link Zip}.
     * @return {@link Flowable} instance.
     * @see #loginProgressDelay(Engine)
     * @see #rxa_inputs(Engine, List)
     * @see #rxa_confirmLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_login(@NotNull final Engine<?> ENGINE,
                                  @NotNull List<Zip<HMTextType,String>> inputs) {
        final LoginActionType THIS = this;

        return rxa_inputs(ENGINE, inputs)
            .flatMap(a -> THIS.rxa_confirmLogin(ENGINE))
            .delay(loginProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
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
