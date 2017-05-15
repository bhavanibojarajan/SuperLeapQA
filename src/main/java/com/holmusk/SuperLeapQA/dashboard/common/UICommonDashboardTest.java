package com.holmusk.SuperLeapQA.dashboard.common;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/16/17.
 */
public abstract class UICommonDashboardTest {
//    @Test
//    @SuppressWarnings("unchecked")
//    public void test_signUpNewAccount_shouldSucceed() {
//        // Setup
//        UserMode mode = userMode();
//        TestSubscriber subscriber = CustomTestSubscriber.create();
//
//        // When
//        rx_splash_personalInfoInput(mode)
//            .concatWith(rxEnterRandomPersonalInfoInputs(mode))
//            .all(BooleanUtil::isTrue)
//            .toFlowable()
//            .doOnNext(a -> LogUtil.println(engine().driver().getPageSource()))
//            .subscribe(subscriber);
//
//        subscriber.awaitTerminalEvent();
//
//        // Then
//        assertCorrectness(subscriber);
//    }

    /**
     * Get the associated {@link UserMode} instance.
     * @return A {@link UserMode} instance.
     */
    @NotNull protected abstract UserMode userMode();
}
