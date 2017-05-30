package com.holmusk.SuperLeapQA.ui.logmeal;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 29/5/17.
 */
public interface UILogMealTestType extends UIBaseTestType, LogMealActionType {
    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * and confirm that all {@link org.openqa.selenium.WebElement} are present.
     * @see com.holmusk.SuperLeapQA.navigation.Screen#SPLASH
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOGIN
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    default void test_logMeal_isValidScreen() {
        // Setup
        final UILogMealTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxv_mealLog(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Test that meal logging works as expected, by following the logging
     * process and posting a meal onto the server.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_MEAL
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_selectMealPhotos(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    default void test_logMeal_shouldWork() {
        // Setup
        final UILogMealTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        UserMode mode = UserMode.PARENT;

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxa_logNewMeal(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
