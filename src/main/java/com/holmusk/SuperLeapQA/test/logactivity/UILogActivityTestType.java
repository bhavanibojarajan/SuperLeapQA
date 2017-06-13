package com.holmusk.SuperLeapQA.test.logactivity;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by haipham on 12/6/17.
 */
public interface UILogActivityTestType extends UIBaseTestType, LogActivityActionType {
    /**
     * Log a new activity and verify that the process works correctly.
     * @see ObjectUtil#nonNull(Object) 
     * @see CSSInput#ACTIVITY
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#ACTIVITY_ENTRY
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #randomSelectableTime()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openCSSTimePicker(Engine, HMCSSInputType)
     * @see #rxa_selectCSSTime(Engine, Date)
     * @see #rxa_confirmCSSTime(Engine, HMCSSInputType)
     * @see #rxa_submitCSSEntry(Engine, HMCSSInputType)
     * @see #rxe_selectedCSSValue(Engine, HMCSSInputType)
     * @see #rxv_hasCSSTime(Engine, Date)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_logActivity_shouldWork(@NotNull UserMode mode) {
        // Setup
        final UILogActivityTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final HMCSSInputType INPUT = CSSInput.ACTIVITY;
        final Date TIME = randomSelectableTime();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.ACTIVITY_ENTRY)
            .flatMap(a -> THIS.rxe_selectedCSSValue(ENGINE, INPUT))
            .flatMap(a -> THIS.rxa_openCSSTimePicker(ENGINE, INPUT)
                .flatMap(b -> THIS.rxa_selectCSSTime(ENGINE, TIME))
                .flatMap(b -> THIS.rxa_confirmCSSTime(ENGINE, INPUT))
                .flatMap(b -> THIS.rxa_submitCSSEntry(ENGINE, INPUT))
                .flatMap(b -> Flowable.mergeArray(THIS.rxv_hasCSSTime(ENGINE, TIME)))
                .all(ObjectUtil::nonNull)
                .toFlowable())
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
