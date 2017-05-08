package com.holmusk.SuperLeapQA.onboarding.welcome;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public class UIWelcomeTest extends UIBaseTest implements WelcomeInteractionType {
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIWelcomeTest(int index) {
        super(index);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_splashScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_welcome()
            .flatMap(a -> rxValidateWelcomeScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
