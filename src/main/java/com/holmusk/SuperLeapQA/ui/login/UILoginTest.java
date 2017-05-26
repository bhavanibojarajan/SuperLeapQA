package com.holmusk.SuperLeapQA.ui.login;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.apache.regexp.RE;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/26/17.
 */
public final class UILoginTest extends UIBaseTest implements
    LoginActionType, RegisterModeActionType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UILoginTest(int index) {
        super(index);
    }

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
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    public void test_loginPage_isValidScreen() {
        // Setup
        final UILoginTest THIS = this;
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
     * Verify that the register page can be accessed from the login page.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#REGISTER
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #assertCorrectness(TestSubscriber)
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    public void test_loginToRegister_shouldWork() {
        // Setup
        final UILoginTest THIS = this;
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
