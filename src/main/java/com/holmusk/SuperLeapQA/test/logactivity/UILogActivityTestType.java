package com.holmusk.SuperLeapQA.test.logactivity;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.ActivityValue;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
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
        Engine<?> engine = engine();
        HMCSSInputType input = CSSInput.ACTIVITY;
        Date time = randomSelectableTime();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.ACTIVITY_ENTRY),
            rxe_selectedCSSValue(engine, input),
            rxa_openCSSTimePicker(engine, input),
            rxa_selectCSSTime(engine, time),
            rxa_confirmCSSTime(engine, input),
            rxa_submitCSSEntry(engine, input),
            rxv_hasCSSTime(engine, time)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Log a new activity and confirm that step calculations are correct. This
     * is only relevant for {@link UserMode#isTeen()}.
     * @see Double#intValue()
     * @see Engine#rxv_errorWithPageSource()
     * @see UserMode#defaultTeenUserMode()
     * @see ActivityValue#TODAY
     * @see Config#STEP_PER_MIN
     * @see CSSInput#ACTIVITY
     * @see DashboardMode#ACTIVITY
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
     * @see Screen#ACTIVITY_ENTRY
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_dashboardMode(Engine, DashboardMode)
     * @see #rxa_submitCSSEntry(Engine, HMCSSInputType)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxe_activityValue(Engine, UserMode, ActivityValue)
     * @see #rxe_selectedCSSNumericValue(Engine, HMCSSInputType)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logActivity_shouldCalculateStepsCorrectly() {
        // Setup
        final UILogActivityTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final HMCSSInputType INPUT = CSSInput.ACTIVITY;
        final DashboardMode DB_MODE = DashboardMode.ACTIVITY;
        final ActivityValue AT_VAL = ActivityValue.TODAY;
        final int STEP_PER_MIN = Config.STEP_PER_MIN;
        final UserMode MODE = UserMode.defaultTeenUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),
            rxa_dashboardMode(ENGINE, DB_MODE),
            rxe_activityValue(ENGINE, MODE, AT_VAL)
                .doOnNext(a -> LogUtil.printft("Today value is now %s", a))
                .flatMap(a -> Flowable.concatArray(
                    THIS.rxa_navigate(MODE, Screen.DASHBOARD, Screen.ACTIVITY_ENTRY),
                    THIS.rxe_selectedCSSNumericValue(ENGINE, INPUT)
                        .doOnNext(b -> LogUtil.printft("Selected value %s", b))
                        .map(b -> b * STEP_PER_MIN)
                        .map(Double::intValue)
                        .flatMap(b -> Flowable.concatArray(
                            THIS.rxa_submitCSSEntry(ENGINE, INPUT),
                            THIS.rxa_backToDashboard(ENGINE),
                            THIS.rxa_dashboardMode(ENGINE, DB_MODE),
                            THIS.rxe_activityValue(ENGINE, MODE, AT_VAL)
                                .doOnNext(c -> LogUtil.printft("Today value is now %s", c))
                                .map(c -> c - a)
                                .map(Double::intValue)
                                .doOnNext(c -> LogUtil.printft("Difference is %d", c))
                                .filter(c -> c.equals(b))
                                .switchIfEmpty(ENGINE.rxv_errorWithPageSource())
                        ))))
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}