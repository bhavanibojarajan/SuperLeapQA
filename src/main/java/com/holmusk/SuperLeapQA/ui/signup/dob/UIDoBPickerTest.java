package com.holmusk.SuperLeapQA.ui.signup.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public final class UIDoBPickerTest extends UIBaseTest implements
    NavigationType, DOBPickerTestHelperType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIDoBPickerTest(int index) {
        super(index);
    }

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
    public void test_DoBPicker_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UIDoBPickerTest THIS = this;
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
        final UIDoBPickerTest THIS = this;
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
     * This test validates that {@link Screen#DOB_PICKER} selection works by
     * sequentially selecting DoBs from a range of {@link java.util.Date}.
     * Note that this test is not guarantor-aware, so
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same.
     * @param MODE {@link UserMode} instance.
     * @see Screen#DOB_PICKER
     * @see #engine()
     * @see UserMode#offsetFromCategoryValidRange(int)
     * @see #rx_h_validateDoBsRecursive(Engine, UserMode, List)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UIDoBPickerTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = MODE.offsetFromCategoryValidRange(2);

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.DOB_PICKER)
            .flatMap(a -> THIS.rx_h_validateDoBsRecursive(ENGINE, MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
