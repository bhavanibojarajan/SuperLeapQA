package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by haipham on 5/10/17.
 */
public final class UIParentSignUpTest extends UIBaseTest implements
    WelcomeActionType,
    RegisterActionType,
    ParentSignUpActionType,
    ParentSignUpValidationType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIParentSignUpTest(int index) {
        super(index);
    }

    /**
     * @return An {@link Integer} value.
     * @see BaseSignUpValidationType#minAcceptableAge()
     */
    @Override
    public int minAcceptableAge() {
        return 5;
    }

    /**
     * @return An {@link Integer} value.
     * @see BaseSignUpValidationType#maxAcceptableAge()
     */
    @Override
    public int maxAcceptableAge() {
        return 6;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentDoBPickerScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_parentDoBPicker()
            .flatMap(a -> rxValidateParentDoBPickerScreen())
            .flatMap(a -> rxNavigateBackWithBackButton())
            .flatMap(a -> rxValidateRegisterScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentDoBPickerDialog_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_parentDoBPicker()
            .flatMap(a -> rxCheckDoBDialogHasCorrectElements())
            .flatMap(a -> rxValidateParentDoBPickerScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentDoBSelection_shouldWork() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = ageOffsetFromAcceptableRange(2);

        // When
        rx_splash_parentDoBPicker()
            .flatMap(a -> rxValidateDoBs(AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentSignUpInputs_shouldShowCorrectInputScreens() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput()
            .flatMap(a -> rxEnterRandomAcceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
