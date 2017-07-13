package com.holmusk.SuperLeapQA.test.forgotpassword;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/26/17.
 */
public interface UIForgotPasswordTestType extends UIBaseTestType, ForgotPasswordActionType {
    /**
     * Verify that the forgot password input succeeds in sending an email
     * and display a notification to the user.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
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
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD),
            rxa_recoverPassword(engine),
            rxv_emailSentConfirmation(engine),
            rxa_confirmEmailSent(engine),
            rxv_loginScreen(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
