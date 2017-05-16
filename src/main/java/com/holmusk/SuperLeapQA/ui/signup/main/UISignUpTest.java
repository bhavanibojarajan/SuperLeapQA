package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public class UISignUpTest extends UIBaseTest implements
    SignUpActionType,
    SignUpValidationType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UISignUpTest(int index) {
        super(index);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_DoBPickerScreen_containsCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(UserMode.PARENT)
            .flatMap(a -> rxValidateParentDoBPickerScreen())
            .flatMap(a -> rxNavigateBackWithBackButton())
            .flatMap(a -> rxValidateRegisterScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_DoBPickerDialog_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> rxCheckDoBDialogHasCorrectElements())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_DoBSelection_shouldWork(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = ageOffsetFromAcceptableRange(mode, 2);

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> rxValidateDoBs(mode, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgeInputs_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> rxConfirmUnacceptableAgeInput())
            .flatMap(a -> rxClickInputField(TextInput.NAME))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgePhoneOrEmail_shouldOnlyRequireOne(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* Check that if phone is entered, we don't need email */
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> rxCheckUnacceptableAgeInputRequired(TextInput.PHONE))

            /* Check that if email is entered, we don't need phone */
            .flatMap(a -> rx_splash_unacceptableAgeInput(mode))
            .flatMap(a -> rxCheckUnacceptableAgeInputRequired(TextInput.EMAIL))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgeInput_shouldWork(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> rxEnterAndValidateUnacceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_acceptableAgeInputs_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(mode)
            .flatMap(a -> rxEnterAndValidateAcceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_acceptableAgeEmptyInputs_showCorrectErrors(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(mode)
            .flatMap(a -> rxValidateAcceptableAgeEmptyInputErrors(mode))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_personalInfoInputScreen_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfoInput(mode)
            .flatMap(a -> rxEnterAndValidatePersonalInfoInputs(mode))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
