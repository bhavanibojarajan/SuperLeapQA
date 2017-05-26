package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 23/5/17.
 */
public final class UIInvalidAgeTest extends UIBaseTest implements
    NavigationType, InvalidAgeTestHelperType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIInvalidAgeTest(int index) {
        super(index);
    }

    /**
     * This test validates that {@link Screen#INVALID_AGE} has the
     * correct {@link org.openqa.selenium.WebElement}, and clicking on the
     * submit button without filling in required inputs, and check that
     * it should fail.
     * @param mode {@link UserMode} instance.
     * @see Screen#INVALID_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #generalUserModeProvider()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_invalidAgeInputs_isValidScreen(@NotNull UserMode mode) {
        // Setup
        final UIInvalidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rx_a_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, TextInput.NAME))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#INVALID_AGE} should only
     * require either {@link TextInput#PHONE} or {@link TextInput#EMAIL},
     * and not both. It sequentially substitutes {@link TextInput} into
     * {@link #rxa_enterInput(Engine, SLInputType, String)}. We do not use
     * {@link DataProvider} with this method because we already have a
     * {@link Factory} for the constructor.
     * @param MODE {@link UserMode} instance.
     * @see Screen#INVALID_AGE
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_h_invalidAgeInputRequired(Engine, TextInput)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_invalidAgeInput_requiresPhoneOrEmail(@NotNull final UserMode MODE) {
        // Setup
        final UIInvalidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* Check that if phone is entered, we don't need email */
        rx_navigate(MODE, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rx_h_invalidAgeInputRequired(ENGINE, TextInput.PHONE))

            /* Check that if email is entered, we don't need phone */
            .flatMap(a -> THIS.rx_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE))
            .flatMap(a -> THIS.rx_h_invalidAgeInputRequired(ENGINE, TextInput.EMAIL))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that filling in {@link Screen#INVALID_AGE}
     * inputs work correctly, by checking that after the submit button is
     * clicked, the user should be brought to the confirm screen and
     * {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see Screen#INVALID_AGE
     * @see Screen#REGISTER
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_enterInvalidAgeInputs(Engine)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_invalidAgeInput_shouldWork(@NotNull UserMode mode) {
        // Setup
        final UIInvalidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rx_a_enterAndConfirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rx_v_invalidAgeInputConfirmed(ENGINE))
            .flatMap(a -> THIS.rx_e_invalidAgeOk(ENGINE))
            .flatMap(ENGINE::rx_click)
            .flatMap(a -> THIS.rx_v_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
