package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeInteractionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeValidationType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.Completable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by haipham on 5/7/17.
 */
public final class UIRegisterTest extends UIBaseTest implements
    WelcomeInteractionType,
    RegisterInteractionType,
    ParentSignUpInteractionType,
    ParentSignUpValidationType
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

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentSignUpScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_parentSignUp()
            .flatMap(a -> rxValidateParentSignUpScreen())

            /* Make sure the back button works */
            .flatMap(a -> rxNavigateBackWithBackButton())
            .flatMap(a -> rxValidateRegisterScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentSignUpDoBSelection_shouldWork() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, 2010);
        Date date = calendar.getTime();

        // When
        rx_splash_parentSignUp()
            .flatMap(a -> rxOpenDoBDialog())
            .flatMap(a -> currentEngine().rxSelectDate(() -> date))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
