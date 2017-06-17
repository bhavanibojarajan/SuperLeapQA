package com.holmusk.SuperLeapQA.test.login;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 5/26/17.
 */
public interface UILoginTestType extends UIBaseTestType, LoginActionType {
    /**
     * Login with predefined credentials and verify that it works correctly.
     * @see ObjectUtil#nonNull(Object)
     * @see UserMode#defaultUserMode()
     * @see UserMode#loginCredentials()
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_login(Engine, List)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_loginInputs_shouldWork() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN),
            rxa_loginWithDefaults(engine, mode)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Verify that the register page can be accessed from the login page.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#REGISTER
     * @see UserMode#defaultUserMode()
     * @see #assertCorrectness(TestSubscriber)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_registerScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_loginToRegister_shouldWork() {
        // Setup
        final UILoginTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.REGISTER)
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
