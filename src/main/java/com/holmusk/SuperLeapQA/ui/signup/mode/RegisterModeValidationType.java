package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeValidationType extends WelcomeValidationType {
    /**
     * Get the sign up button that corresponds to a {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see UserMode#androidRegisterButtonId()
     * @see Engine#rx_containsID(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_signUp(@NotNull Engine<?> engine,
                                             @NotNull UserMode mode) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_containsID(mode.androidRegisterButtonId())
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get the back button's title label.
     * @param engine {@link Engine} instance.
     * @return A {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_backButtonTitle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_containsText("register_title_whichOneBestDescribes")
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Validate the register screen.
     * @param engine {@link Engine} instance.
     * @return A {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see #rx_e_signUp(Engine, UserMode)
     * @see #rx_e_backButtonTitle(Engine)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_registerScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rx_containsText("register_title_iAmParent"),
                engine.rx_containsText("register_title_iRegisterForChild"),
                engine.rx_containsText("register_title_iAmTeen"),
                engine.rx_containsText("register_title_iRegisterForSelf"),
                engine.rx_containsText("register_title_or"),
                engine.rx_containsText("register_title_initiativeByHPB"),
                rx_e_signUp(engine, UserMode.PARENT),
                rx_e_signUp(engine, UserMode.TEEN_UNDER_18),
                rx_e_backButtonTitle(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
