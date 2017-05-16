package com.holmusk.SuperLeapQA.dashboard.common;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/16/17.
 */
public class UICommonDashboardTest extends UIBaseTest implements
    BaseDashboardActionType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UICommonDashboardTest(int index) {
        super(index);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_useAppNow(mode)
            .concatWith(rxValidateUseAppNowScreen())
            .concatWith(rxUseAppNow())
            .concatWith(rxValidateDashboardTutorialScreen())
            .concatWith(rx_dashboardTutorial_dashboard())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
