package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
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
public class UIMainDashboardTest extends UIBaseTest implements DashboardActionType {
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIMainDashboardTest(int index) {
        super(index);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_useApp(mode)
            .flatMap(a -> rxValidateUseAppNowScreen())
            .flatMap(a -> rxUseAppNow())
            .flatMap(a -> rxValidateDashboardTutorialScreen())
            .flatMap(a -> rx_tutorial_dashboard())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
