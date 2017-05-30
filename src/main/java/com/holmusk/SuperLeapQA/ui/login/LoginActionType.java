package com.holmusk.SuperLeapQA.ui.login;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLTextType;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.Engine;

import java.util.List;

/**
 * Created by haipham on 5/26/17.
 */
public interface LoginActionType extends BaseActionType, LoginValidationType {
    /**
     * Confirm login inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_submit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmLogin(@NotNull final Engine<?> ENGINE) {
        /* We need to click twice because on iOS, somehow the click action is
         * not responsive enough, which leads to test failures */
        return rxe_submit(ENGINE).flatMap(a -> ENGINE.rxa_click(a, () -> 2));
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
     * @see #rxa_inputs(Engine, List)
     * @see #rxa_confirmLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_login(@NotNull final Engine<?> ENGINE,
                                  @NotNull List<Zip<SLTextType,String>> inputs) {
        final LoginActionType THIS = this;
        return rxa_inputs(ENGINE, inputs).flatMap(a -> THIS.rxa_confirmLogin(ENGINE));
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
