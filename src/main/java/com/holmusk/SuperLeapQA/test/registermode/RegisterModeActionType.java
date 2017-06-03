package com.holmusk.SuperLeapQA.test.registermode;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeActionType extends RegisterModeValidationType {
    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_signUpMode(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxa_SHAFromRegister(@NotNull final Engine<?> ENGINE,
                                            @NotNull UserMode mode) {
        return rxe_signUpMode(ENGINE, mode).flatMap(ENGINE::rxa_click);
    }
}
