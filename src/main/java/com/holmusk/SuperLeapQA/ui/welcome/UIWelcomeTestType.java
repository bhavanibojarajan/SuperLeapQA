package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/7/17.
 */
public interface UIWelcomeTestType extends UIBaseTestType, WelcomeActionType {
    /**
     * This test validates {@link Screen#WELCOME} by checking that all
     * {@link org.openqa.selenium.WebElement} are visible.
     * @see Screen#WELCOME
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_welcomeScreen(Engine)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    default void test_welcomeScreen_containsCorrectElements() {
        // Setup
        final UIWelcomeTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(UserMode.PARENT, Screen.SPLASH, Screen.WELCOME)
            .flatMap(a -> THIS.rxv_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
