package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.card.CardItemHelperType;
import com.holmusk.SuperLeapQA.test.weightpage.WeightPageActionType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.javautilities.util.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by haipham on 9/6/17.
 */
public interface UILogWeightTestType extends
    UIBaseTestType,
    CardItemHelperType,
    LogWeightActionType,
    WeightPageActionType
{
    /**
     * Validate that weight logging works by posting a new weight card.
     * @see #assertCorrectness(TestSubscriber)
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
        HMCSSInputType input = CSSInput.WEIGHT;
        Date time = randomSelectableTime();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.WEIGHT_ENTRY),

            rxe_selectedCSSValue(ENGINE, input)
                .flatMap(a -> Flowable.concatArray(
                    THIS.rxa_openCSSTimePicker(ENGINE, input),
                    THIS.rxa_selectCSSTime(ENGINE, time),
                    THIS.rxa_confirmCSSTime(ENGINE, input),
                    THIS.rxa_submitCSSEntry(ENGINE, input),
                    THIS.rxv_hasCSSTime(ENGINE, time),
                    THIS.rxa_openEditMenu(ENGINE),
                    THIS.rxa_deleteFromMenu(ENGINE),

                    /* If the weight is deleted, it should not appear as a card
                     * in the dashboard */
                    ENGINE.rxe_containsText(a)
                        .map(BooleanUtil::toTrue)
                        .flatMap(c -> ENGINE.rxv_error())
                        .onErrorReturnItem(true))
                )
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Check that the initial weight is taken into account when the user
     * first logs a weight card, i.e. the change in the first weight page
     * should be non-zero most of the time. We need to delete all weight cards
     * before logging a new weight.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_revealAndDeleteCardItems(Engine, CardType)
     * @see #rxa_toggleDrawer(Engine, boolean)
     * @see #rxa_selectDrawerItem(Engine, DrawerItem)
     * @see #rxa_toggleSetting(Engine, Setting)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxe_weightProgressDisplay(Engine, WeightProgress)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logFirstWeight_shouldComputeCorrectChange() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> E = engine();
        final LocalizerType LOCALIZER = E.localizer();
        final UserMode MODE = UserMode.defaultUserMode();
        CardType card = CardType.WEIGHT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),
            rxa_revealAndDeleteCardItems(E, card),
            rxa_toggleDrawer(E, true),
            rxa_selectDrawerItem(E, DrawerItem.SETTINGS),

            /* Get the initial weight value from settings */
            rxa_toggleSetting(E, Setting.PROFILE),
            rxa_scrollToBottom(E),

            /* We need to do this roundabout way because the initial weight
             * value is not directly readable from the meal page screen on
             * Android. */
            rxe_fieldValue(E, ChoiceInput.START_WEIGHT)
                .compose(RxUtil.removeFromString(LOCALIZER, "uom_title_kg", "uom_title_lbs", " "))
                .map(Double::valueOf)
                .doOnNext(a -> LogUtil.printft("Initial weight: %s", a))
                .flatMap(a -> Flowable.concatArray(
                    /* Click back twice to go back to main settings page */
                    THIS.rxa_clickBackButton(E).repeat(2),
                    THIS.rxa_toggleDrawer(E, false),
                    THIS.rxa_navigate(MODE, Screen.DASHBOARD, Screen.WEIGHT_PAGE),

                    Flowable.zip(
                        THIS.rxe_weightProgress(E, WeightProgress.PREVIOUS),
                        THIS.rxe_weightProgress(E, WeightProgress.CHANGE),
                        NumberUtil::sum
                    ).doOnNext(b -> LogUtil.printft(
                        "Initial weight should be %s, but is %s", b, a)
                    ).filter(b -> b.equals(a)
                    ).switchIfEmpty(E.rxv_error()))
                )
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
