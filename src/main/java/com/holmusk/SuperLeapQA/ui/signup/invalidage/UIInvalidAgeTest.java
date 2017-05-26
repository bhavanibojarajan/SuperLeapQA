package com.holmusk.SuperLeapQA.ui.signup.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by haipham on 23/5/17.
 */
public final class UIInvalidAgeTest extends UIBaseTest implements NavigationType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIInvalidAgeTest(int index) {
        super(index);
    }

    /**
     * Provide data parameters for
     * {@link #test_invalidAgeInput_requiresPhoneOrEmail(UserMode, TextInput)}.
     * @return {@link Iterator} instance.
     * @see #test_invalidAgeInput_requiresPhoneOrEmail(UserMode, TextInput)
     */
    @NotNull
    @DataProvider
    public Iterator<Object[]> userModeInputProvider() {
        return Arrays.asList(
            new Object[] { UserMode.PARENT, TextInput.PHONE },
            new Object[] { UserMode.PARENT, TextInput.EMAIL }
//            new Object[] { UserMode.TEEN_U18, TextInput.PHONE },
//            new Object[] { UserMode.TEEN_U18, TextInput.EMAIL }
        ).iterator();
    }

    /**
     * This test validates that {@link Screen#INVALID_AGE} has the
     * correct {@link org.openqa.selenium.WebElement}, and clicking on the
     * submit button without filling in required inputs, and check that
     * it should fail.
     * @param mode {@link UserMode} instance.
     * @see Screen#INVALID_AGE
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_clickInputField(Engine, SLInputType)
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
        rxa_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rxa_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rxa_clickInputField(ENGINE, TextInput.NAME))
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
     * @param INPUT {@link TextInput} instance.
     * @see Screen#INVALID_AGE
     * @see ObjectUtil#nonNull(Object)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_completeInvalidAgeInput(Engine)
     * @see #rxa_enterRandomInput(Engine, SLTextInputType)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_confirmInvalidAgeInputs(Engine)
     * @see #rxa_watchProgressBarUntilHidden(Engine)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "userModeInputProvider")
    public void test_invalidAgeInput_requiresPhoneOrEmail(@NotNull final UserMode MODE,
                                                          @NotNull final TextInput INPUT) {
        // Setup
        final UIInvalidAgeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMapIterable(a -> Arrays.asList(TextInput.NAME, INPUT))
            .concatMap(a -> THIS.rxa_enterRandomInput(ENGINE, a))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> THIS.rxa_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rxa_watchProgressBarUntilHidden(ENGINE))
            .flatMap(a -> THIS.rxv_invalidAgeInputConfirmed(ENGINE))
            .flatMap(a -> THIS.rxa_completeInvalidAgeInput(ENGINE))
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
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_enterInvalidAgeInputs(Engine)
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
        rxa_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rxa_enterAndConfirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rxv_invalidAgeInputConfirmed(ENGINE))
            .flatMap(a -> THIS.rxe_invalidAgeOk(ENGINE))
            .flatMap(ENGINE::rxa_click)
            .flatMap(a -> THIS.rxv_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
