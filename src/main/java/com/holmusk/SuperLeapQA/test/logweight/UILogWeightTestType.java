package com.holmusk.SuperLeapQA.test.logweight;

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
     * @see Screen#WEIGHT_ENTRY
     * @see UserMode#defaultUserMode()
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
        final HMCSSInputType INPUT = CSSInput.WEIGHT;
        final Date TIME = randomSelectableTime();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.WEIGHT_ENTRY)
            .flatMap(a -> THIS.rxe_selectedCSSValue(ENGINE, INPUT))
            .flatMap(a -> THIS.rxa_openCSSTimePicker(ENGINE, INPUT)
                .flatMap(b -> THIS.rxa_selectCSSTime(ENGINE, TIME))
                .flatMap(b -> THIS.rxa_confirmCSSTime(ENGINE, INPUT))
                .flatMap(b -> THIS.rxa_submitCSSEntry(ENGINE, INPUT))
                .flatMap(b -> Flowable.mergeArray(THIS.rxv_hasCSSTime(ENGINE, TIME)))
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
}
