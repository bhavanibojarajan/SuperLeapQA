package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Date;

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
     * @see #defaultUserMode()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_completeWeightValue(Engine)
     * @see #rxa_openWeightTimePicker(Engine)
     * @see #rxa_selectWeightTime(Engine, Date)
     * @see #rxa_confirmWeightTime(Engine)
     * @see #rxa_submitWeightEntry(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeight_shouldWork() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final Date TIME = randomSelectableTime();
        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_WEIGHT)
            .flatMap(a -> THIS.rxa_completeWeightValue(ENGINE))
            .flatMap(a -> THIS.rxa_openWeightTimePicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectWeightTime(ENGINE, TIME))
            .flatMap(a -> THIS.rxa_confirmWeightTime(ENGINE))
            .flatMap(a -> THIS.rxa_submitWeightEntry(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
