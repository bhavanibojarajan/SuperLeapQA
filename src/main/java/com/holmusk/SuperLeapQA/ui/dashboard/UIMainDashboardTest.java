package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.signup.invalidage.InvalidAgeTestHelperType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/16/17.
 */
public class UIMainDashboardTest extends UIBaseTest implements DashboardActionType, InvalidAgeTestHelperType {
    @Factory(
        dataProviderClass = Runner.class,
        dataProvider = "dataProvider"
    )
    public UIMainDashboardTest(int index) {
        super(index);
    }

    /**
     * This test builds upon
     * {@link com.holmusk.SuperLeapQA.ui.signup.personalinfo.UIPersonalInfoTest#test_guarantorNeeded_requiresParentInfo(UserMode)},
     * but also provides validations for {@link Screen#DASHBOARD}. It will
     * check {@link Screen#USE_APP_NOW} and {@link Screen#DASHBOARD_TUTORIAL}
     * as well.
     * @param mode {@link UserMode} instance.
     * @see Screen#USE_APP_NOW
     * @see Screen#DASHBOARD_TUTORIAL
     * @see Screen#DASHBOARD
     * @see #engine()
     * @see #rx_v_useAppNowScreen(Engine)
     * @see #rxa_useAppNow(Engine)
     * @see #rx_v_dashboardTutorialScreen(Engine)
     * @see #guarantorSpecificUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = true)
    @Test(dataProvider = "guarantorSpecificUserModeProvider")
    public void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
        // Setup
        final UIMainDashboardTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.DOB, Screen.USE_APP_NOW)
            .flatMap(a -> THIS.rx_v_useAppNowScreen(ENGINE))
            .flatMap(a -> THIS.rxa_useAppNow(ENGINE))
            .flatMap(a -> THIS.rx_v_dashboardTutorialScreen(ENGINE))
            .flatMap(a -> THIS.rxa_navigate(mode, Screen.DASHBOARD_TUTORIAL, Screen.DASHBOARD))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
