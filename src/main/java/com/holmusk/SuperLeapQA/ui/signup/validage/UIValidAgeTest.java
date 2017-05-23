package com.holmusk.SuperLeapQA.ui.signup.validage;

import com.holmusk.SuperLeapQA.model.ChoiceInput;
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

/**
 * Created by haipham on 23/5/17.
 */
public final class UIValidAgeTest extends UIBaseTest implements
    NavigationType, ValidAgeTestHelperType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIValidAgeTest(int index) {
        super(index);
    }

    /**
     * This test validates that {@link Screen#VALID_AGE} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * @param MODE {@link UserMode} instance.
     * @see Screen#VALID_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_enterAndCheckValidAgeInputs(Engine, UserMode)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_validAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIValidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rx_h_enterAndCheckValidAgeInputs(ENGINE, MODE))
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
        final UIValidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rx_h_checkInchToFootRecursive(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#VALID_AGE} inputs show
     * the correct empty input errors, by sequentially entering/selecting
     * inputs and clicking the confirm button. If the inputs are not completed,
     * the user will be notified.
     * @param MODE {@link UserMode} instance.
     * @see Screen#VALID_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_validAgeEmptyInputErrors(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_validAgeEmptyInputs_showsCorrectErrors(@NotNull final UserMode MODE) {
        // Setup
        final UIValidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rx_h_validAgeEmptyInputErrors(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
