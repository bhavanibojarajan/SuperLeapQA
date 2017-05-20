package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/16/17.
 */
public class UIMainDashboardTest extends UIBaseTest implements DashboardTestHelperType {
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIMainDashboardTest(int index) {
        super(index);
    }

//    /**
//     * This test builds upon
//     * {@link com.holmusk.SuperLeapQA.ui.signup.main.UISignUpTest#test_guarantorNeeded_shouldRequireParentInfo(UserMode)},
//     * but also provides validations for the dashboard screen. It will check
//     * the Use App Now popup and the dashboard tutorial as well.
//     * @param mode {@link UserMode} instance.
//     * @see #rx_splash_useApp(UserMode)
//     * @see #rxValidateUseAppNowScreen()
//     * @see #rxUseAppNow()
//     * @see #rxValidateDashboardTutorialScreen()
//     * @see #rx_tutorial_dashboard()
//     * @see #guarantorSpecificUserModeProvider()
//     */
//    @SuppressWarnings("unchecked")
//    @GuarantorAware(value = true)
//    @Test(dataProvider = "guarantorSpecificUserModeProvider")
//    public void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
//        // Setup
//        final UIMainDashboardTest THIS = this;
//        TestSubscriber subscriber = CustomTestSubscriber.create();
//
//        // When
//        rx_splash_useApp(mode)
//            .flatMap(a -> THIS.rxValidateUseAppNowScreen())
//            .flatMap(a -> THIS.rx_a_useAppNow())
//            .flatMap(a -> THIS.rxValidateDashboardTutorialScreen())
//            .flatMap(a -> THIS.rx_n_tutorial_dashboard())
//            .subscribe(subscriber);
//
//        subscriber.awaitTerminalEvent();
//
//        // Then
//        assertCorrectness(subscriber);
//    }
}
