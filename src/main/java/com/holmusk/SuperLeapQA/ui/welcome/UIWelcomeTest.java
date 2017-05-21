package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.model.Screen;
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
public class UIWelcomeTest extends UIBaseTest implements WelcomeActionType {
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIWelcomeTest(int index) {
        super(index);
    }

    /**
     * This test validates the welcome screen by checking that all
     * {@link org.openqa.selenium.WebElement} are visible.
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_v_welcomeScreen(Engine)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    public void test_welcomeScreen_containsCorrectElements() {
        // Setup
        final UIWelcomeTest THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(UserMode.PARENT, Screen.SPLASH, Screen.WELCOME)
            .flatMap(a -> THIS.rx_v_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
