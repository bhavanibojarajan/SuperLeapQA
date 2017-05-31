package com.holmusk.SuperLeapQA.ui.forgotpassword;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/26/17.
 */
public interface UIForgotPasswordTestType extends UIBaseTestType, ForgotPasswordActionType {
    /**
     * Verify that {@link Screen#FORGOT_PASSWORD} has valid
     * {@link org.openqa.selenium.WebElement} by checking their visibility
     * and interacting with them.
     * @see Screen#SPLASH
     * @see Screen#FORGOT_PASSWORD
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_forgotPassword(Engine)
     * @see #engine()
     */
    @SuppressWarnings("unchecked")
    @Test(groups = "ValidateScreen")
    @GuarantorAware(value = false)
    default void test_forgotPassword_isValidScreen() {
        // Setup
        final UIForgotPasswordTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD)
            .flatMap(a -> THIS.rxv_forgotPassword(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Verify that the forgot password input succeeds in sending an email
     * and display a notification to the user.
     * @see Screen#SPLASH
     * @see Screen#FORGOT_PASSWORD
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_recoverPassword(Engine)
     * @see #rxa_confirmEmailSent(Engine)
     * @see #rxv_emailSentConfirmation(Engine)
     * @see #rxv_loginScreen(Engine)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalDelay()
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    default void test_forgotPasswordInput_shouldWork() {
        // Setup
        final UIForgotPasswordTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        UserMode mode = UserMode.PARENT;

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD)
            .flatMap(a -> THIS.rxa_recoverPassword(ENGINE))
            .flatMap(a -> THIS.rxa_watchProgressBar(ENGINE))
            .flatMap(a -> THIS.rxv_emailSentConfirmation(ENGINE))
            .flatMap(a -> THIS.rxa_confirmEmailSent(ENGINE))
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxv_loginScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
