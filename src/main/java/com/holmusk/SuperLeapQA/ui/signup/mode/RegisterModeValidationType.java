package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterModeValidationType extends WelcomeValidationType {
    /**
     * Get the sign up button that corresponds to a {@link UserMode}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#androidRegisterButtonId()
     * @see BaseEngine#rxElementContainingID(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxSignUpButton(@NotNull UserMode mode) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingID(mode.androidRegisterButtonId());
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the back button's title label.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxBackButtonTitleLabel() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingText("register_title_whichOneBestDescribes");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Validate the register screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateRegisterScreen() {
        final BaseEngine<?> ENGINE = engine();

        return ENGINE.rxElementContainingText("register_title_iAmParent")
            .flatMap(a -> ENGINE.rxElementContainingText("register_title_iRegisterForChild"))
            .flatMap(a -> ENGINE.rxElementContainingText("register_title_iAmTeen"))
            .flatMap(a -> ENGINE.rxElementContainingText("register_title_iRegisterForSelf"))
            .flatMap(a -> ENGINE.rxElementContainingText("register_title_or"))
            .flatMap(a -> ENGINE.rxElementContainingText("register_title_initiativeByHPB"))
            .flatMap(a -> rxSignUpButton(UserMode.PARENT))
            .flatMap(a -> rxSignUpButton(UserMode.TEEN_UNDER_18))
            .flatMap(a -> rxBackButtonTitleLabel())
            .map(BooleanUtil::toTrue);
    }
}
