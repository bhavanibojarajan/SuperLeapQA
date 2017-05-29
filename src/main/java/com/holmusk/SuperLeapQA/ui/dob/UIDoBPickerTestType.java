package com.holmusk.SuperLeapQA.ui.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIDoBPickerTestType extends UIBaseTestType, DOBPickerTestHelperType {
    /**
     * This test checks that {@link Screen#DOB} has correct elements,
     * by checking that all {@link org.openqa.selenium.WebElement} are present
     * and back navigation shows {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see Screen#REGISTER
     * @see Screen#DOB
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxh_DoBPickerScreen(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_registerScreen(Engine)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider",
        groups = "ValidateScreen"
    )
    default void test_DoBPicker_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UIDoBPickerTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> THIS.rxh_DoBPickerScreen(ENGINE))
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks {@link Screen#DOB} dialog has the correct
     * elements, by verifying that all required
     * {@link org.openqa.selenium.WebElement} are present. It selects a random
     * {@link java.util.Date} with which to interact with the calendar/date
     * picker.
     * @param MODE {@link UserMode} instance.
     * @see Screen#DOB
     * @see Screen#INVALID_AGE
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxa_selectDoB(Engine, Date)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_DoBEditFieldHasDate(Engine, Date)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider",
        groups = "ValidateScreen"
    )
    default void test_DoBPickerDialog_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIDoBPickerTestType THIS = this;
        final Engine<?> ENGINE = engine();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> THIS.rxa_openDoBPicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectDoB(ENGINE, DATE))
            .flatMap(a -> THIS.rxa_confirmDoB(ENGINE))
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_DoBEditFieldHasDate(ENGINE, DATE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#DOB} selection works by
     * sequentially selecting DoBs from a range of {@link java.util.Date}.
     * Note that this test is not guarantor-aware, so
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same.
     * @param MODE {@link UserMode} instance.
     * @see Screen#DOB
     * @see #engine()
     * @see UserMode#offsetFromCategoryValidRange(int)
     * @see #rxh_validateDoBsRecursive(Engine, UserMode, List)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UIDoBPickerTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = MODE.offsetFromCategoryValidRange(2);

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> THIS.rxh_validateDoBsRecursive(ENGINE, MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}