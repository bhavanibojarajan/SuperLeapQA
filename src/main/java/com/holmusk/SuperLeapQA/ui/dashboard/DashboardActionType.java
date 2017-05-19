package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.signup.main.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardActionType extends
    UnacceptableAgeActionType,
    PersonalInfoActionType,
    DashboardValidationType
{
    //region Bridged Navigation
    /**
     * Bridge method to navigate from splash to the dashboard tutorial screen,
     * by registering a new account.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_useApp(UserMode)
     * @see #rx_useApp_tutorial()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_tutorial(@NotNull UserMode mode) {
        final DashboardActionType THIS = this;
        return rx_splash_useApp(mode).flatMap(a -> THIS.rx_useApp_tutorial());
    }

    /**
     * Bridge method to navigate from splash to the dashboard screen, by
     * registering a new account.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_tutorial(UserMode)
     * @see #rx_tutorial_dashboard()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_signUp_dashboard(@NotNull UserMode mode) {
        final DashboardActionType THIS = this;
        return rx_splash_tutorial(mode).flatMap(a -> THIS.rx_tutorial_dashboard());
    }
    //endregion

    /**
     * Click the Use App Now button.
     * @return A {@link Flowable} instance.
     * @see #rxUseAppNowButton()
     * @see BaseEngine#rxClick(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxUseAppNow() {
        final BaseEngine<?> ENGINE = engine();
        return rxUseAppNowButton().flatMap(ENGINE::rxClick).map(BooleanUtil::toTrue);
    }

    /**
     * Navigate from the Use App Now screen to the Dashboard tutorial screen,
     * assuming the user is already in the Use App Now screen.
     * @return A {@link Flowable} instance.
     * @see #rxUseAppNow()
     */
    @NotNull
    default Flowable<Boolean> rx_useApp_tutorial() {
        return rxUseAppNow();
    }

    /**
     * Navigate from the dashboard tutorial to the dashboard, assuming the
     * user is in the tutorial screen (i.e. using for the first time).
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxNavigateBackOnce()
     */
    @NotNull
    default Flowable<Boolean> rx_tutorial_dashboard() {
        return engine().rxNavigateBackOnce();
    }
}
