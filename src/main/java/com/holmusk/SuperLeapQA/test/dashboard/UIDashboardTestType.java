package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by haipham on 5/16/17.
 */
public interface UIDashboardTestType extends UIBaseTestType, DashboardActionType {
    /**
     * Provide {@link CardType} that can be deleted.
     * @return {@link Iterator} instance.
     * @see CardType#WEIGHT
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> deletableCardProvider() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[] { UserMode.PARENT, CardType.MEAL });
        data.add(new Object[] { UserMode.PARENT, CardType.WEIGHT });
        data.add(new Object[] { UserMode.TEEN_A18, CardType.MEAL });
        data.add(new Object[] { UserMode.TEEN_U18, CardType.WEIGHT });
        return data.iterator();
    }

    /**
     * Delete all cards for a particular {@link CardType}.
     * If we want to use this method as an utility method, we should run it
     * with {@link org.swiften.xtestkit.mobile.Platform#ANDROID} since it's
     * much faster.
     * @param mode {@link UserMode} instance.
     * @param card {@link CardType} instance.
     * @see BooleanUtil#isTrue(boolean)
     * @see ObjectUtil#nonNull(Object)
     * @see RxUtil#repeatWhile(Flowable)
     * @see Screen#SPLASH
     * @see Screen#DASHBOARD
     * @see #deletableCardProvider()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_revealCardList(Engine)
     * @see #rxa_dashboardCardTab(Engine, CardType)
     * @see #rxa_firstCardItem(Engine, CardType)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxv_cardListEmpty(Engine, CardType)
     * @see #rxv_cardListEmpty(Engine, CardType)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIDashboardTestType.class,
        dataProvider = "deletableCardProvider"
    )
    default void test_deleteCards_shouldWork(@NotNull UserMode mode,
                                             @NotNull CardType card) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.DASHBOARD),
            rxa_revealCardList(engine),
            rxa_dashboardCardTab(engine, card),

            Flowable
                .concatArray(
                    rxa_firstCardItem(engine, card),
                    rxa_openEditMenu(engine),
                    rxa_deleteFromMenu(engine)
                )
                .all(ObjectUtil::nonNull).toFlowable()
                .compose(RxUtil.repeatWhile(rxv_cardListNotEmpty(engine, card)))
                .onErrorReturnItem(true),

            rxv_cardListEmpty(engine, card)
                .filter(BooleanUtil::isTrue)
                .switchIfEmpty(engine.rxv_errorWithPageSource())
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
