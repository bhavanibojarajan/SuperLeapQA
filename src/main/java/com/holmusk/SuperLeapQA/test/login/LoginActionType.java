package com.holmusk.SuperLeapQA.test.login;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
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
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_forgotPassword(Engine)
     */
    @NotNull
    default Flowable<?> rxa_forgotPasswordFromLogin(@NotNull Engine<?> engine) {
        return rxe_forgotPassword(engine).compose(engine.clickFn());
    }

    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_loginRegister(Engine)
     */
    @NotNull
    default Flowable<?> rxa_registerFromLogin(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_loginRegister(engine).compose(engine.clickFn());
        } else if (engine instanceof IOSEngine) {
            return rxe_loginRegister(engine).compose(engine.tapMiddleFn());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Confirm login inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_watchUntilHidden(Flowable)
     * @see #loginProgressDelay(Engine)
     * @see #rxe_submit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmLogin(@NotNull Engine<?> engine) {
        return Flowable.concatArray(
            rxe_submit(engine).compose(engine.clickFn()),
            Flowable.timer(loginProgressDelay(engine), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter random login credentials.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_loginInputs(@NotNull Engine<?> engine) {
        return rxa_randomInputs(engine, TextInput.EMAIL, TextInput.PASSWORD);
    }

    /**
     * Enter and confirm login credentials.
     * @param engine {@link Engine} instance.
     * @param inputs {@link List} of {@link Tuple}.
     * @return {@link Flowable} instance.
     * @see #rxa_inputs(Engine, List)
     * @see #rxa_confirmLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_login(@NotNull Engine<?> engine,
                                  @NotNull List<Tuple<HMTextType,String>> inputs) {
        return Flowable
            .concatArray(rxa_inputs(engine, inputs), rxa_confirmLogin(engine))
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Login with default credentials, as supplied by
     * {@link UserMode#loginCredentials()}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_login(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_loginWithDefaults(@NotNull Engine<?> engine,
                                              @NotNull UserMode mode) {
        return rxa_login(engine, mode.loginCredentials());
    }
}
