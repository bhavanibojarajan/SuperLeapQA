package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 9/6/17.
 */
public interface UILogWeightTestType extends UIBaseTestType, LogWeightActionType {
    /**
     * Validate that weight logging works by posting a new weight card.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_WEIGHT
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_completeWeightValue(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeight_shouldWork() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_WEIGHT)
            .flatMap(a -> THIS.rxa_completeWeightValue(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
