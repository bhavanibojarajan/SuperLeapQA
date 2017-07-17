package com.holmusk.SuperLeapQA.test.registermode;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.HPObjects;
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
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_signUpMode(@NotNull Engine<?> engine,
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
    default Flowable<WebElement> rxe_backButtonTitle(@NotNull Engine<?> engine) {
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
     * @see #rxe_signUpMode(Engine, UserMode)
     * @see #rxe_backButtonTitle(Engine)
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
                rxe_signUpMode(engine, UserMode.PARENT),
                rxe_signUpMode(engine, UserMode.TEEN_U18),
                rxe_backButtonTitle(engine)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }
}
