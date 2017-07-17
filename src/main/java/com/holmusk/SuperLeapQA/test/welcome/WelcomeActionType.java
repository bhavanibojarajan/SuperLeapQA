package com.holmusk.SuperLeapQA.test.welcome;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_welcomeLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_loginFromWelcome(@NotNull Engine<?> engine) {
        return rxe_welcomeLogin(engine).compose(engine.clickFn());
    }

    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_welcomeRegister(Engine)
     */
    @NotNull
    default Flowable<?> rxa_registerFromWelcome(@NotNull Engine<?> engine) {
        return rxe_welcomeRegister(engine).compose(engine.clickFn());
    }
}
