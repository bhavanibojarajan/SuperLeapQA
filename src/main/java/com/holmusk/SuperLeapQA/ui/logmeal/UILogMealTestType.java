package com.holmusk.SuperLeapQA.ui.logmeal;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Test;

/**
 * Created by haipham on 29/5/17.
 */
public interface UILogMealTestType extends UIBaseTestType {
    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * and confirm that all {@link org.openqa.selenium.WebElement} are present.
     * @see com.holmusk.SuperLeapQA.navigation.Screen#SPLASH
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOGIN
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL
     */
    @SuppressWarnings("unchecked")
    @Test(groups = "ValidateScreen")
    default void test_logMeal_isValidScreen() {
        // Setup
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
