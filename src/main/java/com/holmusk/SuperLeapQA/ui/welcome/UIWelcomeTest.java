package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
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
     * @see #rx_splash_welcome()
     * @see #rxValidateWelcomeScreen()
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    public void test_splashScreen_shouldContainCorrectElements() {
        // Setup
        final UIWelcomeTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_welcome()
            .flatMap(a -> THIS.rxValidateWelcomeScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
