package com.holmusk.SuperLeapQA.onboarding.splash;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.openqa.selenium.By;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public class UIOnboardingTest extends UIBaseTest implements
    OnboardingInteractionType,
    OnboardingValidationType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIOnboardingTest(int index) {
        super(index);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_splashScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_wait()
            .flatMap(a -> rxValidateViews())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
