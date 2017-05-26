package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public final class UIRegisterModeTest extends UIBaseTest implements RegisterModeActionType {
    @Factory(
        dataProviderClass = Runner.class,
        dataProvider = "dataProvider"
    )
    public UIRegisterModeTest(int index) {
        super(index);
    }

    /**
     * This test validates that {@link Screen#REGISTER} contains the correct
     * {@link org.openqa.selenium.WebElement} by checking their visibility,
     * and then navigate back once to check {@link Screen#WELCOME}.
     * @see Screen#REGISTER
     * @see Screen#WELCOME
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_registerScreen(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_welcomeScreen(Engine)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    public void test_registerScreen_isValidScreen() {
        // Setup
        final UIRegisterModeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(UserMode.PARENT, Screen.SPLASH, Screen.REGISTER)
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))

            /* Make sure the back button works */
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
