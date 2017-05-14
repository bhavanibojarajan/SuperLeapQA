package com.holmusk.SuperLeapQA.onboarding.common;

import com.holmusk.SuperLeapQA.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public abstract class UICommonSignUpTest extends UIBaseTest implements
    BaseSignUpActionType,
    BaseSignUpValidationType
{
    public UICommonSignUpTest(int index) {
        super(index);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentDoBPickerDialog_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(signUpMode())
            .concatWith(rxCheckDoBDialogHasCorrectElements())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_DoBSelection_shouldWork() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = ageOffsetFromAcceptableRange(2);

        // When
        rx_splash_DoBPicker(UserMode.PARENT)
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
    public void test_unacceptableAgeInputs_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(signUpMode())
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
    public void test_unacceptableAgePhoneInput_shouldBeRequired() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(signUpMode())
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
    public void test_unacceptableAgeInput_shouldWork() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(signUpMode())
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
    public void test_acceptableAgeInputs_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(signUpMode())
            .concatWith(rxValidateRandomAcceptableAgeInputs())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_acceptableAgeEmptyInputs_shouldShowCorrectErrors() {
        // Setup
        UserMode mode = signUpMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAgeInput(mode)
            .concatWith(rxConfirmAcceptableAgeEmptyInputErrors(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_personalInfoInputScreen_shouldHaveCorrectElements() {
        // Setup
        UserMode mode = signUpMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfoInput(mode)
            .doOnNext(a -> LogUtil.println(engine().driver().getPageSource()))
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Get the {@link UserMode} being used for testing.
     * @return A {@link UserMode} instance.
     */
    protected abstract UserMode signUpMode();
}
