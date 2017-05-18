package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import com.holmusk.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
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
    SignUpValidationType,
    DOBPickerActionType,
    UnacceptableAgeInputActionType,
    AcceptableAgeInputType,
    PersonalInfoInputActionType
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

    //region Validate screen correctness
    /**
     * This test checks that the DoB screen has correct elements, by checking
     * that all {@link org.openqa.selenium.WebElement} are present and back
     * navigation shows the correct register screen.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rxValidateDoBPickerScreen()
     * @see #rxNavigateBackWithBackButton()
     * @see #rxValidateRegisterScreen()
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_DoBPickerScreen_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rxValidateDoBPickerScreen())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxValidateRegisterScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the DoB picker dialog has the correct elements,
     * by verifying that all required {@link org.openqa.selenium.WebElement}
     * are present. It selects a random {@link java.util.Date} with which
     * to interact with the calendar/date picker.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rxCheckDoBDialogHasCorrectElements()
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
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

    /**
     * This test validates that the unacceptable age input screen has the
     * correct {@link org.openqa.selenium.WebElement}, and clicking on the
     * submit button without filling in require inputs should fail.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_unacceptableAgeInput(UserMode)
     * @see #rxClickInputField(InputType)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
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

    /**
     * This test validates that the acceptable age inputs screen contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rxEnterAndValidateAcceptableAgeInputs()
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_acceptableAgeInputs_containsCorrectElements(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAge(mode)
            .flatMap(a -> THIS.rxEnterAndValidateAcceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the personal info input screen contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility and interacting with each of them.
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_personalInfo(UserMode)
     * @see #rxEnterAndValidatePersonalInfo(UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_personalInfoScreen_containsCorrectElements(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfo(MODE)
            .flatMap(a -> THIS.rxValidatePersonalInfoScreen(MODE))
            .flatMap(a -> THIS.rxEnterAndValidatePersonalInfo(MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
    //endregion

    /**
     * This test validates that DoB selection works by sequentially selecting
     * DoBs from a range of {@link java.util.Date}.
     * Note that this test is not guarantor-aware, so
     * {@link UserMode#TEEN_UNDER_18} and {@link UserMode#TEEN_ABOVE_18} will
     * be treated the same.
     * @param MODE A {@link UserMode} instance.
     * @see UserMode#offsetFromCategoryAcceptableRange(int)
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rxValidateDoBs(UserMode, List)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = MODE.offsetFromCategoryAcceptableRange(2);

        // When
        rx_splash_DoBPicker(MODE)
            .flatMap(a -> THIS.rxValidateDoBs(MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the unacceptable age inputs should only
     * require either {@link TextInput#PHONE} or {@link TextInput#EMAIL},
     * and not both. It sequentially substitutes {@link TextInput} into
     * {@link #rxEnterInput(TextInputType, String)}. We do not use
     * {@link DataProvider} with this method because we already have a
     * {@link Factory} for the constructor.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_unacceptableAgeInput(UserMode)
     * @see #rxCheckUnacceptableAgeInputRequired(TextInput)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_unacceptableAgeInput_shouldRequirePhoneOrEmail(@NotNull UserMode mode) {
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

    /**
     * This test validates that filling in unacceptable age inputs work
     * correctly, by checking that after the submit button is clicked, the
     * user should be brought to the confirm screen and the register page.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_unacceptableAgeInput(UserMode)
     * @see #rxEnterAndValidateUnacceptableAgeInputs()
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
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

    /**
     * This test validates that the acceptable age inputs show the correct
     * empty input errors, by sequentially entering/selecting inputs and
     * clicking the confirm button. If the inputs are not completed, the
     * user will be notified.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rxValidateAcceptableAgeEmptyInputErrors(UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_acceptableAgeEmptyInputs_showCorrectErrors(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAge(mode)
            .flatMap(a -> THIS.rxValidateAcceptableAgeEmptyInputErrors(mode))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the personal info inputs for parents/guarantors
     * should only require either {@link TextInput#PARENT_MOBILE} or
     * {@link TextInput#PARENT_EMAIL}. This test is only applicable for
     * {@link UserMode#TEEN_UNDER_18}, so we use a {@link DataProvider}
     * that provides {@link InputType}.
     * @param inputs A {@link List} of {@link InputType}.
     * @see #rx_splash_extraInfo(UserMode)
     * @see #rxEnterPersonalInfo(UserMode)
     * @see #rxConfirmExtraPersonalInfo(UserMode)
     * @see #rxProgressBar()
     * @see #parentPersonalInfoProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "parentPersonalInfoProvider")
    public void test_parentInfoScreen_shouldRequirePhoneOrEmail(@NotNull List<SLInputType> inputs) {
        // Setup
        final UISignUpTest THIS = this;
        final UserMode MODE = UserMode.TEEN_UNDER_18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_extraInfo(MODE)
            .flatMap(a -> THIS.rxEnterPersonalInfo(inputs))
            .flatMap(a -> THIS.rxConfirmExtraPersonalInfo(MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rxProgressBar())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TOC and opens up
     * the Web browser, the personal info inputs are saved and then restored
     * when the user gets back to the app. This is more relevant for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_personalInfo(UserMode)
     * @see #rxValidatePersonalInfoStateSaved(UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_navigateAwayFromPersonalInfo_shouldSaveState(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfo(MODE)
            .flatMap(a -> THIS.rxValidatePersonalInfoStateSaved(MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link UserMode#TEEN_UNDER_18} will see the
     * parent information screen, while {@link UserMode#TEEN_ABOVE_18} will
     * not. It uses a custom {@link DataProvider} that provides only
     * {@link UserMode#TEEN_UNDER_18} and {@link UserMode#TEEN_ABOVE_18}.
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_useApp(UserMode)
     * @see #guarantorSpecificUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = true)
    @Test(dataProvider = "guarantorSpecificUserModeProvider")
    public void test_guarantorNeeded_shouldRequireParentInfo(@NotNull final UserMode MODE) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* During the tests, if the current user requires a guarantor (i.e
         * below 18 years-old), we expect the parent information screen to
         * be present */
        rx_splash_useApp(MODE).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the TOC checkbox has to be ticked before the
     * user continues any further. The check happens in the personal info
     * input screen.
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_personalInfo(UserMode)
     * @see #rxValidateTOCCheckedBeforeProceeding(UserMode)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_requireTOCAccepted_toProceedFurther(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfo(MODE)
            .flatMap(a -> THIS.rxValidateTOCCheckedBeforeProceeding(MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
