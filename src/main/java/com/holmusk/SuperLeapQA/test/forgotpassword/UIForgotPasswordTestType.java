package com.holmusk.SuperLeapQA.test.forgotpassword;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
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
     * Verify that the forgot password input succeeds in sending an email
     * and display a notification to the user.
     * @see Screen#SPLASH
     * @see Screen#FORGOT_PASSWORD
     * @see #assertCorrectness(TestSubscriber)
     * @see #defaultUserMode()
     * @see #engine()
     * @see #forgotPasswordProgressDelay(Engine)
     * @see #generalDelay(Engine)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_recoverPassword(Engine)
     * @see #rxa_confirmEmailSent(Engine)
     * @see #rxv_emailSentConfirmation(Engine)
     * @see #rxv_loginScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_forgotPasswordInput_shouldWork() {
        // Setup
        final UIForgotPasswordTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD)
            .flatMap(a -> THIS.rxa_recoverPassword(ENGINE))
            .delay(forgotPasswordProgressDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxv_emailSentConfirmation(ENGINE))
            .flatMap(a -> THIS.rxa_confirmEmailSent(ENGINE))
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxv_loginScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
