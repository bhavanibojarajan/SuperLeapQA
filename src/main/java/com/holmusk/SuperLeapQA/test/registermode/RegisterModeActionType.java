package com.holmusk.SuperLeapQA.test.registermode;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeActionType extends RegisterModeValidationType {
    /**
     * Navigate to {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_signUpMode(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxa_SHAFromRegister(@NotNull Engine<?> engine,
                                            @NotNull UserMode mode) {
        if (engine instanceof AndroidEngine) {
            return rxe_signUpMode(engine, mode).compose(engine.clickFn());
        } else if (engine instanceof IOSEngine) {
            /* On iOS, tapping on the element can be rather flaky. Therefore,
             * we tap on its middle coordinate instead. */
            return rxe_signUpMode(engine, mode).compose(engine.tapMiddleFn());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
