package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public final class UIRegisterModeTest extends UIBaseTest implements
    WelcomeActionType,
    RegisterModeActionType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIRegisterModeTest(int index) {
        super(index);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_registerScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_register()
            .flatMap(a -> rxValidateRegisterScreen())

            /* Make sure the back button works */
            .flatMap(a -> rxNavigateBackWithBackButton())
            .flatMap(a -> rxValidateWelcomeScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
