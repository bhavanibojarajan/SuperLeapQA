package com.holmusk.SuperLeapQA.ui.signup.mode;

import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.TestRunner;
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
        dataProviderClass = TestRunner.class,
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
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_v_registerScreen(Engine)
     * @see #rx_a_clickBackButton(Engine)
     * @see #rx_v_welcomeScreen(Engine)
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
        rx_navigate(UserMode.PARENT, Screen.SPLASH, Screen.REGISTER)
            .flatMap(a -> THIS.rx_v_registerScreen(ENGINE))

            /* Make sure the back button works */
            .flatMap(a -> THIS.rx_a_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rx_v_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
