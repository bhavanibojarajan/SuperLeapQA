package com.holmusk.SuperLeapQA.ui.signup.mode;

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
public final class UIRegisterModeTest extends UIBaseTest implements RegisterModeActionType {
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIRegisterModeTest(int index) {
        super(index);
    }

    /**
     * This test validates that the register mode screen contains the correct
     * {@link org.openqa.selenium.WebElement} by checking their visibility,
     * and then navigate back once to check the welcome screen.
     * @see #rx_splash_register()
     * @see #rxValidateRegisterScreen()
     * @see #rxNavigateBackWithBackButton()
     * @see #rxValidateWelcomeScreen()
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    public void test_registerScreen_shouldContainCorrectElements() {
        // Setup
        final UIRegisterModeTest THIS = this;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_register()
            .flatMap(a -> THIS.rx_v_registerScreen())

            /* Make sure the back button works */
            .flatMap(a -> THIS.rx_a_clickBackButton())
            .flatMap(a -> THIS.rxValidateWelcomeScreen())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
