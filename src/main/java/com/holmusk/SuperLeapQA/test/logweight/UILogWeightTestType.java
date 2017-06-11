package com.holmusk.SuperLeapQA.test.logweight;

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
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_WEIGHT_ENTRY
     * @see #assertCorrectness(TestSubscriber)
     * @see #defaultUserMode()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openWeightTimePicker(Engine)
     * @see #rxa_selectWeightTime(Engine, Date)
     * @see #rxa_confirmWeightTime(Engine)
     * @see #rxa_submitWeightEntry(Engine)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxe_selectedWeight(Engine)
     * @see #rxv_hasWeightTime(Engine, Date)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeightAndDelete_shouldWork() {
        // Setup
        final UILogWeightTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final Date TIME = randomSelectableTime();
        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_WEIGHT_ENTRY)
            .flatMap(a -> THIS.rxe_selectedWeight(ENGINE))

            /* Get the weight for later checks */
            .flatMap(a -> THIS.rxa_openWeightTimePicker(ENGINE)
                .flatMap(b -> THIS.rxa_selectWeightTime(ENGINE, TIME))
                .flatMap(b -> THIS.rxa_confirmWeightTime(ENGINE))
                .flatMap(b -> THIS.rxa_submitWeightEntry(ENGINE))
                .flatMap(b -> Flowable.mergeArray(
                    THIS.rxv_hasWeightTime(ENGINE, TIME)
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
}
