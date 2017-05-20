package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
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
     * @see BaseEngine#rx_elementsContainingID(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxSignUpButton(@NotNull UserMode mode) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine
                .rx_elementsContainingID(mode.androidRegisterButtonId())
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get the back button's title label.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementsContainingText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxBackButtonTitleLabel() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine
                .rx_elementsContainingText("register_title_whichOneBestDescribes")
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Validate the register screen.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementsContainingText(String...)
     * @see #rxSignUpButton(UserMode)
     * @see #rxBackButtonTitleLabel()
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateRegisterScreen() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .mergeArray(
                ENGINE.rx_elementsContainingText("register_title_iAmParent"),
                ENGINE.rx_elementsContainingText("register_title_iRegisterForChild"),
                ENGINE.rx_elementsContainingText("register_title_iAmTeen"),
                ENGINE.rx_elementsContainingText("register_title_iRegisterForSelf"),
                ENGINE.rx_elementsContainingText("register_title_or"),
                ENGINE.rx_elementsContainingText("register_title_initiativeByHPB"),
                rxSignUpButton(UserMode.PARENT),
                rxSignUpButton(UserMode.TEEN_UNDER_18),
                rxBackButtonTitleLabel()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
