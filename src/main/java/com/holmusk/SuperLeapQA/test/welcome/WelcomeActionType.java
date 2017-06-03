package com.holmusk.SuperLeapQA.test.welcome;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeActionType extends BaseActionType, WelcomeValidationType {
    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_welcomeLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxa_loginFromWelcome(@NotNull final Engine<?> ENGINE) {
        return rxe_welcomeLogin(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_welcomeRegister(Engine)
     */
    @NotNull
    default Flowable<?> rxa_registerFromWelcome(@NotNull final Engine<?> ENGINE) {
        return rxe_welcomeRegister(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
