package com.holmusk.SuperLeapQA.test.css;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.weightpage.WeightPageActionType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by haipham on 9/6/17.
 */
public interface UILogWeightTestType extends
    UIBaseTestType,
    LogWeightActionType,
    WeightPageActionType
{
    /**
     * Validate that weight logging works by posting a new weight card.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxv_errorWithPageSource()
     * @see ObjectUtil#nonNull(Object)
     * @see CSSInput#WEIGHT
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_WEIGHT_ENTRY
     * @see #assertCorrectness(TestSubscriber)
     * @see #defaultUserMode()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openCSSTimePicker(Engine, HMCSSInputType)
     * @see #rxa_selectCSSTime(Engine, Date)
     * @see #rxa_confirmCSSTime(Engine, HMCSSInputType)
     * @see #rxa_submitCSSEntry(Engine, HMCSSInputType)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxe_selectedCSSValue(Engine, HMCSSInputType)
     * @see #rxv_hasCSSTime(Engine, Date)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeightAndDelete_shouldWork() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final HMCSSInputType INPUT = CSSInput.WEIGHT;
        final Date TIME = randomSelectableTime();
        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_WEIGHT_ENTRY)
            .flatMap(a -> THIS.rxe_selectedCSSValue(ENGINE, INPUT))

            /* Get the weight for later checks */
            .flatMap(a -> THIS.rxa_openCSSTimePicker(ENGINE, INPUT)
                .flatMap(b -> THIS.rxa_selectCSSTime(ENGINE, TIME))
                .flatMap(b -> THIS.rxa_confirmCSSTime(ENGINE, INPUT))
                .flatMap(b -> THIS.rxa_submitCSSEntry(ENGINE, INPUT))
                .flatMap(b -> Flowable.mergeArray(
                    THIS.rxv_hasCSSTime(ENGINE, TIME)
                ))
                .all(ObjectUtil::nonNull)
                .toFlowable()
                .flatMap(b -> THIS.rxa_openEditMenu(ENGINE))
                .flatMap(b -> THIS.rxa_deleteFromMenu(ENGINE))

                /* If the weight is deleted, it should not appear as a card
                 * in the dashboard */
                .flatMap(b -> ENGINE.rxe_containsText(a)
                    .map(BooleanUtil::toTrue)
                    .flatMap(c -> ENGINE.rxv_errorWithPageSource())
                    .onErrorReturnItem(true)))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Log some weight cards and verify that the previous weights are saved
     * the next time the user logs another weight.
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxv_errorWithPageSource()
     * @see ObjectUtil#nonNull(Object)
     * @see CSSInput#WEIGHT
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
     * @see Screen#LOG_WEIGHT_VALUE
     * @see Screen#LOG_WEIGHT_ENTRY
     * @see #defaultUserMode()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_submitCSSValue(Engine, HMCSSInputType)
     * @see #rxa_submitCSSEntry(Engine, HMCSSInputType)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxa_dashboardFromWeightEntry(Engine)
     * @see #rxe_selectedCSSValue(Engine, HMCSSInputType)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeight_shouldStartFromPreviousWeight() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final int TRIES = 3;
        final HMCSSInputType INPUT = CSSInput.WEIGHT;
        final UserMode MODE = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD)
            .concatMap(a -> Flowable.range(0, TRIES))
            .concatMap(a -> THIS
                .rxa_navigate(MODE, Screen.DASHBOARD, Screen.LOG_WEIGHT_ENTRY)

                /* For cross-platform reusability, we need to get the weight
                 * value from the weight entry screen, since on Android it's
                 * not possible to get it directly from the weight scroll */
                .concatMap(b -> THIS.rxe_selectedCSSValue(ENGINE, INPUT))
                .concatMap(b -> THIS.rxa_submitCSSEntry(ENGINE, INPUT)
                    .flatMap(c -> THIS.rxa_backToDashboard(ENGINE))
                    .flatMap(c -> THIS.rxa_navigate(MODE,
                        Screen.DASHBOARD, Screen.LOG_WEIGHT_VALUE)
                    )
                    .flatMap(c -> THIS.rxa_submitCSSValue(ENGINE, INPUT))

                    /* The weight value should have been saved from the previous
                     * log */
                    .flatMap(c -> ENGINE.rxe_containsText(b)
                        .firstElement().toFlowable()
                        .switchIfEmpty(ENGINE.rxv_errorWithPageSource()))
                    .flatMap(c -> THIS.rxa_dashboardFromWeightEntry(ENGINE))))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
