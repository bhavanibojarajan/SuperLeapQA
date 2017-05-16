package com.holmusk.SuperLeapQA.dashboard.common;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.onboarding.common.BaseSignUpActionType;
import io.reactivex.Flowable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 5/16/17.
 */
public interface BaseDashboardActionType extends
    BaseSignUpActionType,
    BaseDashboardValidationType
{
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to the use app
     * now screen, by registering a new account.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_personalInfoInput(UserMode)
     * @see #rx_personalInfoInput_useAppNow(UserMode)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_useAppNow(@NotNull UserMode mode) {
        return rx_splash_personalInfoInput(mode)
            .concatWith(rx_personalInfoInput_useAppNow(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method to navigate from splash to the dashboard tutorial screen,
     * by registering a new account.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_useAppNow(UserMode)
     * @see #rx_useAppNow_dashboardTutorial()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_dashboardTutorial(@NotNull UserMode mode) {
        return rx_splash_useAppNow(mode)
            .concatWith(rx_useAppNow_dashboardTutorial())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method to navigate from splash to the dashboard screen, by
     * registering a new account.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_dashboardTutorial(UserMode)
     * @see #rx_dashboardTutorial_dashboard()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_signUp_dashboard(@NotNull UserMode mode) {
        return rx_splash_dashboardTutorial(mode)
            .concatWith(rx_dashboardTutorial_dashboard())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
    //endregion

    /**
     * Click the Use App Now button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxUseAppNow() {
        final BaseEngine<?> ENGINE = engine();

        return rxUseAppNowButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Navigate from the personal info input screen to the dashboard screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_personalInfoInput(UserMode)
     * @see #rxConfirmPersonalInfoInputs()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_personalInfoInput_useAppNow(@NotNull UserMode mode) {
        final BaseSignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return rxEnterRandomPersonalInfoInputs(mode)
            .concatWith(rxConfirmPersonalInfoInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()

            /* First progress bar appears immediately after the submit button
             * is clicked */
            .concatWith(THIS.rxWatchUntilProgressBarNoLongerVisible())

            /* There is a short delay between the first and the second
             * progress bar */
            .concatWith(THIS.rxWatchPersonalInfoScreenUntilNoLongerVisible())

            /* The second progress bar appears */
            .concatWith(THIS.rxWatchUntilProgressBarNoLongerVisible())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Navigate from the Use App Now screen to the Dashboard tutorial screen,
     * assuming the user is already in the Use App Now screen.
     * @return A {@link Flowable} instance.
     * @see #rxUseAppNow()
     */
    @NotNull
    default Flowable<Boolean> rx_useAppNow_dashboardTutorial() {
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
    default Flowable<Boolean> rx_dashboardTutorial_dashboard() {
        return engine().rxNavigateBackOnce();
    }
}
