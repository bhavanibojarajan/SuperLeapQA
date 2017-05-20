package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.ChoiceInput;
import com.holmusk.SuperLeapQA.model.SLInputType;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
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
    DOBPickerTestHelperType,
    AcceptableAgeTestHelperType,
    UnacceptableAgeTestHelperType,
    PersonalInfoTestHelperType
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
    public void test_DoBPickerScreen_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rx_h_DoBPickerScreen())
            .flatMap(a -> THIS.rx_a_clickBackButton())
            .flatMap(a -> THIS.rx_v_registerScreen())
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
    public void test_DoBPickerDialog_isValidScreen(@NotNull UserMode mode) {
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
     * @see #rx_a_clickInputField(SLInputType)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_unacceptableAgeInputs_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_unacceptableAgeInput(mode)
            .flatMap(a -> THIS.rx_a_confirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rx_a_clickInputField(TextInput.NAME))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the acceptable age inputs screen contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rxEnterAndValidateAcceptableAgeInputs(UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_acceptableAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAge(MODE)
            .flatMap(a -> THIS.rx_h_enterAndCheckAcceptableAgeInputs(MODE))
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
    public void test_personalInfoScreen_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfo(MODE)
            .flatMap(a -> THIS.rx_v_personalInfoScreen(MODE))
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
     * @see #rx_h_validateDoBs(UserMode, List)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_h_validateDoBs(MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the unacceptable age inputs should only
     * require either {@link TextInput#PHONE} or {@link TextInput#EMAIL},
     * and not both. It sequentially substitutes {@link TextInput} into
     * {@link #rxEnterInput(SLInputType, String)}. We do not use
     * {@link DataProvider} with this method because we already have a
     * {@link Factory} for the constructor.
     * @param mode A {@link UserMode} instance.
     * @see #rx_splash_unacceptableAgeInput(UserMode)
     * @see #rxCheckUnacceptableAgeInputRequired(TextInput)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_h_unacceptableAgeInputRequired(TextInput.PHONE))

            /* Check that if email is entered, we don't need phone */
            .flatMap(a -> THIS.rx_splash_unacceptableAgeInput(mode))
            .flatMap(a -> THIS.rx_h_unacceptableAgeInputRequired(TextInput.EMAIL))
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
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_h_enterAndCheckUnacceptableAgeInputs())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Confirm that when the user selects
     * {@link ChoiceInput#HEIGHT} in
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}, every 12
     * {@link com.holmusk.SuperLeapQA.model.Height#INCH} is converted to
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}.
     * @param MODE A {@link UserMode} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rx_validate12InchConvertedToAFoot(UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_12Inch_shouldBeConvertedTo1Foot(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_acceptableAge(MODE)
            .flatMap(a -> THIS.rx_validate12InchConvertedToAFoot(MODE))
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
     * @see #rx_h_acceptableAgeEmptyInputErrors(UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_h_acceptableAgeEmptyInputErrors(mode))
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
     * @see #rx_enterPersonalInfo(UserMode)
     * @see #rx_confirmExtraPersonalInfo(UserMode)
     * @see #rx_progressBar()
     * @see #parentPersonalInfoProvider()
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_enterPersonalInfo(inputs))
            .flatMap(a -> THIS.rx_a_confirmExtraPersonalInfo(MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rx_progressBar())
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
     * @see #rx_validatePersonalInfoStateSaved(UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_leavePersonalInfo_shouldSaveState(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_personalInfo(MODE)
            .flatMap(a -> THIS.rx_validatePersonalInfoStateSaved(MODE))
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
     * @see #assertCorrectness(TestSubscriber)
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
     * @see #rx_validateTOCCheckedBeforeProceeding(UserMode)
     * @see #assertCorrectness(TestSubscriber)
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
            .flatMap(a -> THIS.rx_validateTOCCheckedBeforeProceeding(MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
