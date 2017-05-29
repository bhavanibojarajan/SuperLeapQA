package com.holmusk.SuperLeapQA.ui.login;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.ui.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 5/26/17.
 */
public interface UILoginTestType extends UIBaseTestType, LoginActionType, RegisterModeActionType {
    /**
     * Check that {@link Screen#LOGIN} has valid
     * {@link org.openqa.selenium.WebElement}, by checking their visibility
     * and interacting with them.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_loginScreen(Engine)
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    default void test_loginPage_isValidScreen() {
        // Setup
        final UILoginTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN)
            .flatMap(a -> THIS.rxv_loginScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Login with predefined credentials and verify that it works correctly.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see UserMode#loginCredentials()
     * @see #rxa_login(Engine, List)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     */
    @Test
    @GuarantorAware(value = false)
    @SuppressWarnings("unchecked")
    default void test_loginInputs_shouldWork() {
        // Setup
        final UILoginTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN)
            .flatMap(a -> THIS.rxa_loginWithDefaults(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Verify that the register page can be accessed from the login page.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#REGISTER
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_registerScreen(Engine)
     * @see #assertCorrectness(TestSubscriber)
     */
    @Test
    @GuarantorAware(value = false)
    @SuppressWarnings("unchecked")
    default void test_loginToRegister_shouldWork() {
        // Setup
        final UILoginTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        UserMode mode = UserMode.PARENT;

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.REGISTER)
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
