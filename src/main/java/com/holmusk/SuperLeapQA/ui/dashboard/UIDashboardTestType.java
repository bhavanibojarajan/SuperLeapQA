package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.ui.personalinfo.UIPersonalInfoTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/16/17.
 */
public interface UIDashboardTestType extends UIBaseTestType, DashboardActionType {
    /**
     * This test builds upon
     * {@link UIPersonalInfoTestType#test_guarantorNeeded_requiresParentInfo(UserMode)},
     * but also provides validations for {@link Screen#DASHBOARD}. It will
     * check {@link Screen#USE_APP_NOW} and {@link Screen#DASHBOARD_TUTORIAL}
     * as well.
     * @param mode {@link UserMode} instance.
     * @see Screen#USE_APP_NOW
     * @see Screen#DASHBOARD_TUTORIAL
     * @see Screen#DASHBOARD
     * @see #engine()
     * @see #rxv_useAppNow(Engine)
     * @see #rxa_useAppNow(Engine)
     * @see #rxv_dashboardTutorial(Engine)
     * @see #guarantorSpecificUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = true)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "guarantorSpecificUserModeProvider"
    )
    default void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
        // Setup
        final UIDashboardTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.DOB, Screen.USE_APP_NOW)
            .flatMap(a -> THIS.rxv_useAppNow(ENGINE))
            .flatMap(a -> THIS.rxa_useAppNow(ENGINE))
            .flatMap(a -> THIS.rxv_dashboardTutorial(ENGINE))
            .flatMap(a -> THIS.rxa_navigate(mode, Screen.DASHBOARD_TUTORIAL, Screen.DASHBOARD))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates login with different default credentials that
     * correspond to each {@link UserMode}, and confirms that
     * {@link Screen#DASHBOARD} should appear differently depending on the
     * {@link UserMode}.
     * @param MODE {@link UserMode} instance.
     * @see DashboardMode#BMI
     * @see DashboardMode#ACTIVITY
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_dashboardMode(Engine, DashboardMode)
     * @see #rxv_dashboardBMI(Engine)
     * @see #rxv_dashboardActivity(Engine, UserMode)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_loginToDashboard_shouldSucceed(@NotNull final UserMode MODE) {
        // Setup
        final UIDashboardTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD)
            .flatMap(a -> THIS.rxv_dashboard(ENGINE))
            .flatMap(a -> THIS.rxa_dashboardMode(ENGINE, DashboardMode.BMI))
            .flatMap(a -> THIS.rxv_dashboardBMI(ENGINE))
            .flatMap(a -> THIS.rxa_dashboardMode(ENGINE, DashboardMode.ACTIVITY))
            .flatMap(a -> THIS.rxv_dashboardActivity(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
