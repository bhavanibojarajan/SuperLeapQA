package com.holmusk.SuperLeapQA.test.registermode;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

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
     * @see Engine#rxa_tapMiddle(WebElement)
     * @see #rxe_signUpMode(Engine, UserMode)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_SHAFromRegister(@NotNull final Engine<?> ENGINE,
                                            @NotNull UserMode mode) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_signUpMode(ENGINE, mode).flatMap(ENGINE::rxa_click);
        } else if (ENGINE instanceof IOSEngine) {
            /* On iOS, tapping on the element can be rather flaky. Therefore,
             * we tap on its middle coordinate instead. */
            return rxe_signUpMode(ENGINE, mode).flatMap(ENGINE::rxa_tapMiddle);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
