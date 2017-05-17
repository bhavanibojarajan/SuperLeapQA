package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.InputType;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
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

    @DataProvider
    public Iterator<Object[]> parentPersonalInfoProvider() {
        List<Object[]> data = new LinkedList<>();

        data.add(new Object[] {
            Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_EMAIL)
        });

        data.add(new Object[] {
            Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_MOBILE)
        });

        return data.iterator();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_DoBPickerScreen_containsCorrectElements() {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(UserMode.PARENT)
            .flatMap(a -> THIS.rxValidateParentDoBPickerScreen())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxValidateRegisterScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_DoBPickerDialog_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rxCheckDoBDialogHasCorrectElements())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_DoBSelection_shouldWork(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = ageOffsetFromAcceptableRange(mode, 2);

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rxValidateDoBs(mode, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgeInputs_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rxClickInputField(TextInput.NAME))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgePhoneOrEmail_shouldOnlyRequireOne(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* Check that if phone is entered, we don't need email */
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> THIS.rxCheckUnacceptableAgeInputRequired(TextInput.PHONE))

            /* Check that if email is entered, we don't need phone */
            .flatMap(a -> THIS.rx_splash_unacceptableAgeInput(mode))
            .flatMap(a -> THIS.rxCheckUnacceptableAgeInputRequired(TextInput.EMAIL))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgeInput_shouldWork(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> THIS.rxEnterAndValidateUnacceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_acceptableAgeInputs_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(mode)
            .flatMap(a -> THIS.rxEnterAndValidateAcceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_acceptableAgeEmptyInputs_showCorrectErrors(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(mode)
            .flatMap(a -> THIS.rxValidateAcceptableAgeEmptyInputErrors(mode))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_personalInfoInputScreen_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfoInput(mode)
            .flatMap(a -> THIS.rxEnterAndValidatePersonalInfoInputs(mode))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "parentPersonalInfoProvider")
    public void test_parentInfoInputScreen_shouldOnlyRequirePhoneOrEmail(@NotNull List<InputType> inputs) {
        // Setup
        final UISignUpTest THIS = this;
        final UserMode MODE = UserMode.TEEN;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_extraInfoInput(MODE)
            .flatMap(a -> THIS.rxEnterRandomPersonalInfoInputs(inputs))
            .flatMap(a -> THIS.rxConfirmExtraPersonalInputs(MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rxProgressBar())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
    }
}
