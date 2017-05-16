package com.holmusk.SuperLeapQA.ui.signup;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.runner.TestRunner;
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

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_DoBPickerDialog_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(mode)
            .concatWith(rxCheckDoBDialogHasCorrectElements())
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxValidateDoBs(mode, AGES))
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxConfirmUnacceptableAgeInput())
            .concatWith(rxClickInputField(TextInput.NAME))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
    public void test_unacceptableAgePhoneInput_shouldBeRequired(@NotNull UserMode mode) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .concatWith(rxCheckUnacceptableAgePhoneInputIsRequired(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxEnterAndValidateUnacceptableAgeInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxEnterAndValidateAcceptableAgeInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxValidateAcceptableAgeEmptyInputErrors(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable()
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
            .concatWith(rxEnterAndValidatePersonalInfoInputs(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
