package com.holmusk.SuperLeapQA.ui.mode;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeValidationType extends WelcomeValidationType {
    /**
     * Get the sign up button that corresponds to {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#registerButtonText()
     * @see Engine#rxe_containsText(String...)
     * @see RxUtil#error(String)
     */
    @NotNull
    default Flowable<WebElement> rxe_signUp(@NotNull Engine<?> engine,
                                            @NotNull UserMode mode) {
        return engine
            .rxe_containsText(mode.registerButtonText())
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the back button's title label.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_backButtonTitle(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_whichOneBestDescribes")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate the register screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_signUp(Engine, UserMode)
     * @see #rx_e_backButtonTitle(Engine)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_registerScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("register_title_iAmParent"),
                engine.rxe_containsText("register_title_iRegisterForChild"),
                engine.rxe_containsText("register_title_iAmTeen"),
                engine.rxe_containsText("register_title_iRegisterForSelf"),
                engine.rxe_containsText("register_title_or"),
                engine.rxe_containsText("register_title_initiativeByHPB"),
                rxe_signUp(engine, UserMode.PARENT),
                rxe_signUp(engine, UserMode.TEEN_U18),
                rx_e_backButtonTitle(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
