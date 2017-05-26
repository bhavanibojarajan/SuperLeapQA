package com.holmusk.SuperLeapQA.ui.login;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/26/17.
 */
public interface LoginActionType extends LoginValidationType {
    /**
     * Confirm login inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_submit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmLoginInputs(@NotNull final Engine<?> ENGINE) {
        return rxe_submit(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
