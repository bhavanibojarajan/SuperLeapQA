package com.holmusk.SuperLeapQA.test.popup;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 6/19/17.
 */
public interface UIPopupTestType extends UIBaseTestType {
    /**
     * Check that popup polling works as expected, i.e. when there is a popup,
     * it should be dismissed immediately.
     * This is only relevant for {@link org.swiften.xtestkit.mobile.Platform#IOS}.
     * Under normal circumstances, this test should be disabled since it's just
     * used to check the correctness of the polling.
     * @see HPObjects#nonNull(Object)
     * @see UserMode#defaultUserMode()
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_popupPolling_shouldWork() {
        // Setup
        Engine<?> engine = engine();

        if (!(engine instanceof IOSEngine)) {
            return;
        }

        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),
            Flowable.timer(100000, TimeUnit.MILLISECONDS)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
