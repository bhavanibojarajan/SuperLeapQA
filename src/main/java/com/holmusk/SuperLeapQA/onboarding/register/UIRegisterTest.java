package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public final class UIRegisterTest extends UIBaseTest implements
    WelcomeActionType,
    RegisterActionType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIRegisterTest(int index) {
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
