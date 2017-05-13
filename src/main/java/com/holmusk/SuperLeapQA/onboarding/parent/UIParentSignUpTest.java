package com.holmusk.SuperLeapQA.onboarding.parent;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.onboarding.common.BaseSignUpValidationType;
import com.holmusk.SuperLeapQA.onboarding.parent.ParentSignUpActionType;
import com.holmusk.SuperLeapQA.onboarding.parent.ParentSignUpValidationType;
import com.holmusk.SuperLeapQA.onboarding.register.RegisterActionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

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
            .concatWith(rxValidateParentDoBPickerScreen())
            .concatWith(rxNavigateBackWithBackButton())
            .concatWith(rxValidateRegisterScreen())
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxCheckDoBDialogHasCorrectElements())
            .concatWith(rxValidateParentDoBPickerScreen())
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxValidateDoBs(AGES))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentUnacceptableAgeInputs_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput()
            .concatWith(rxConfirmUnacceptableAgeInput())
            .concatWith(rxClickInputField(TextInput.NAME))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentUnacceptableAgePhoneInput_shouldBeRequired() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput()
            .concatWith(rxCheckUnacceptableAgePhoneInputIsRequired())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentUnacceptableAgeInput_shouldWork() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput()
            .concatWith(rxEnterAndValidateUnacceptableAgeInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentAcceptableAgeInputs_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput()
            .concatWith(rxEnterRandomAcceptableAgeInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
