package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
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
     * This test checks that {@link Screen#DOB_PICKER} has correct elements,
     * by checking that all {@link org.openqa.selenium.WebElement} are present
     * and back navigation shows {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see Screen#REGISTER
     * @see Screen#DOB_PICKER
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_DoBPickerScreen(Engine)
     * @see #rx_a_clickBackButton(Engine)
     * @see #rx_v_registerScreen(Engine)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_DoBPickerScreen_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.DOB_PICKER)
            .flatMap(a -> THIS.rx_h_DoBPickerScreen(ENGINE))
            .flatMap(a -> THIS.rx_a_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rx_v_registerScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks {@link Screen#DOB_PICKER} dialog has the correct
     * elements, by verifying that all required
     * {@link org.openqa.selenium.WebElement} are present. It selects a random
     * {@link java.util.Date} with which to interact with the calendar/date
     * picker.
     * @param mode {@link UserMode} instance.
     * @see Screen#DOB_PICKER
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_checkDoBDialogElements(Engine)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_DoBPickerDialog_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.DOB_PICKER)
            .flatMap(a -> THIS.rx_h_checkDoBDialogElements(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#UNACCEPTABLE_AGE} has the
     * correct {@link org.openqa.selenium.WebElement}, and clicking on the
     * submit button without filling in required inputs, and check that
     * it should fail.
     * @param mode {@link UserMode} instance.
     * @see Screen#UNACCEPTABLE_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_unacceptableAgeInputs_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.UNACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_a_confirmUnacceptableAgeInput(ENGINE))
            .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, TextInput.NAME))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#ACCEPTABLE_AGE} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * @param MODE {@link UserMode} instance.
     * @see Screen#ACCEPTABLE_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_enterAndCheckAcceptableAgeInputs(Engine, UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_acceptableAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.ACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_enterAndCheckAcceptableAgeInputs(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#PERSONAL_INFO} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility and interacting with each of them.
     * @param MODE {@link UserMode} instance.
     * @see Screen#PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_enterAndCheckPersonalInfo(Engine, UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_personalInfoScreen_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rx_v_personalInfoScreen(ENGINE, MODE))
            .flatMap(a -> THIS.rx_h_enterAndCheckPersonalInfo(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
    //endregion

    /**
     * This test validates that {@link Screen#DOB_PICKER} selection works by
     * sequentially selecting DoBs from a range of {@link java.util.Date}.
     * Note that this test is not guarantor-aware, so
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same.
     * @param MODE {@link UserMode} instance.
     * @see Screen#DOB_PICKER
     * @see #engine()
     * @see UserMode#offsetFromCategoryAcceptableRange(int)
     * @see #rx_h_validateDoBs(Engine, UserMode, List)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = MODE.offsetFromCategoryAcceptableRange(2);

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.DOB_PICKER)
            .flatMap(a -> THIS.rx_h_validateDoBs(ENGINE, MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#UNACCEPTABLE_AGE} should only
     * require either {@link TextInput#PHONE} or {@link TextInput#EMAIL},
     * and not both. It sequentially substitutes {@link TextInput} into
     * {@link #rx_a_enterInput(Engine, SLInputType, String)}. We do not use
     * {@link DataProvider} with this method because we already have a
     * {@link Factory} for the constructor.
     * @param MODE {@link UserMode} instance.
     * @see Screen#UNACCEPTABLE_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_unacceptableAgeInputRequired(Engine, TextInput)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_unacceptableAgeInput_shouldRequirePhoneOrEmail(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* Check that if phone is entered, we don't need email */
        rx_navigate(MODE, Screen.SPLASH, Screen.UNACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_unacceptableAgeInputRequired(ENGINE, TextInput.PHONE))

            /* Check that if email is entered, we don't need phone */
            .flatMap(a -> THIS.rx_navigate(MODE, Screen.SPLASH, Screen.ACCEPTABLE_AGE))
            .flatMap(a -> THIS.rx_h_unacceptableAgeInputRequired(ENGINE, TextInput.EMAIL))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that filling in {@link Screen#UNACCEPTABLE_AGE}
     * inputs work correctly, by checking that after the submit button is
     * clicked, the user should be brought to the confirm screen and
     * {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see Screen#UNACCEPTABLE_AGE
     * @see Screen#REGISTER
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_enterAndCheckUnacceptableAgeInputs(Engine)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_unacceptableAgeInput_shouldWork(@NotNull UserMode mode) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.UNACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_enterAndCheckUnacceptableAgeInputs(ENGINE))
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
     * @param MODE {@link UserMode} instance.
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_checkInchToFootRecursive(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_12Inch_shouldBeConvertedTo1Foot(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.ACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_checkInchToFootRecursive(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#ACCEPTABLE_AGE} inputs show
     * the correct empty input errors, by sequentially entering/selecting
     * inputs and clicking the confirm button. If the inputs are not completed,
     * the user will be notified.
     * @param MODE {@link UserMode} instance.
     * @see Screen#ACCEPTABLE_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_acceptableAgeEmptyInputErrors(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_acceptableAgeEmptyInputs_showCorrectErrors(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.ACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_acceptableAgeEmptyInputErrors(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#EXTRA_PERSONAL_INFO} for
     * parents/guarantors should only require either
     * {@link TextInput#PARENT_MOBILE} or {@link TextInput#PARENT_EMAIL}.
     * This test is only applicable for {@link UserMode#TEEN_U18}, so we use
     * {@link DataProvider} that provides {@link InputType}.
     * @param INPUTS {@link List} of {@link InputType}.
     * @see Screen#EXTRA_PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see #rx_a_confirmExtraPersonalInfo(Engine, UserMode)
     * @see #rx_e_progressBar(Engine)
     * @see #parentPersonalInfoProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "parentPersonalInfoProvider")
    public void test_parentInfoScreen_shouldRequirePhoneOrEmail(@NotNull final List<SLInputType> INPUTS) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.TEEN_U18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.EXTRA_PERSONAL_INFO)
            .flatMap(a -> THIS.rx_a_enterPersonalInfo(ENGINE, INPUTS))
            .flatMap(a -> THIS.rx_a_confirmExtraPersonalInfo(ENGINE, MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rx_e_progressBar(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TOC and opens up
     * the Web browser, {@link Screen#PERSONAL_INFO} inputs are saved and then
     * restored when the user gets back to the app. This is more relevant for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#PERSONAL_INFO
     * @see org.swiften.xtestkit.mobile.Platform#ANDROID
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_checkPersonalInfoStateSaved(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_leavePersonalInfo_shouldSaveState(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rx_h_checkPersonalInfoStateSaved(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link UserMode#TEEN_U18} will see
     * {@link Screen#EXTRA_PERSONAL_INFO}, while {@link UserMode#TEEN_A18} will
     * not. It uses a custom {@link DataProvider} that provides only
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#EXTRA_PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
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
        rx_navigate(MODE, Screen.SPLASH, Screen.USE_APP_NOW).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the TOC checkbox has to be ticked before the
     * user continues any further. The check happens in
     * {@link Screen#PERSONAL_INFO}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_checkTOCCBeforeProceeding(Engine, UserMode)
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_requireTOCAccepted_toProceedFurther(@NotNull final UserMode MODE) {
        // Setup
        final UISignUpTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.ACCEPTABLE_AGE)
            .flatMap(a -> THIS.rx_h_checkTOCCBeforeProceeding(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
