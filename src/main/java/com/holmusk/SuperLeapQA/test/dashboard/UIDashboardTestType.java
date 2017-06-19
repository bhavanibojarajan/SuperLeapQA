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
import org.swiften.javautilities.test.TestNGUtil;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * Created by haipham on 5/16/17.
 */
public interface UIDashboardTestType extends UIBaseTestType, DashboardActionType {
    /**
     * Provide {@link CardType} that can be deleted.
     * @return {@link Iterator} instance.
     * @see TestNGUtil#oneFromEach(Object[]...)
     * @see CardType#MEAL
     * @see CardType#WEIGHT
     * @see UserMode#PARENT
     * @see UserMode#TEEN_A18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> deletableCardProvider() {
        return TestNGUtil.oneFromEach(
            new Object[] {
                UserMode.PARENT
//                UserMode.TEEN_A18
            },

            new Object[] {
//                CardType.MEAL
                CardType.WEIGHT
            }
        ).iterator();
    }

    /**
     * Delete all cards for a particular {@link CardType}.
     * If we want to use this method as an utility method, we should run it
     * with {@link org.swiften.xtestkit.mobile.Platform#ANDROID} since it's
     * much faster.
     * @param mode {@link UserMode} instance.
     * @param card {@link CardType} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#DASHBOARD
     * @see #deletableCardProvider()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_revealAndDeleteCardItems(Engine, CardType)
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
            rxa_revealAndDeleteCardItems(engine, card)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Complete steps to delete the first item corresponding to {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_firstCardItem(Engine, CardType)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxn_cardItemPageInitialized(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_deleteFirstCardItem(@NotNull Engine<?> engine,
                                                @NotNull CardType card) {
        return Flowable
            .concatArray(
                rxa_firstCardItem(engine, card),
                rxn_cardItemPageInitialized(engine, card),
                rxa_openEditMenu(engine),
                rxa_deleteFromMenu(engine)
            )
            .all(ObjectUtil::nonNull).toFlowable()
            .onErrorReturnItem(true);
    }

    /**
     * Delete all items for {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isTrue(boolean)
     * @see Engine#rxv_errorWithPageSource()
     * @see ObjectUtil#nonNull(Object)
     * @see RxUtil#repeatWhile(Flowable)
     * @see #rxa_deleteFirstCardItem(Engine, CardType)
     * @see #rxv_cardListEmpty(Engine, CardType)
     * @see #rxv_cardListNotEmpty(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_deleteAllCardItems(@NotNull Engine<?> engine,
                                               @NotNull CardType card) {
        return Flowable.concatArray(
            rxa_deleteFirstCardItem(engine, card)
                .compose(RxUtil.repeatWhile(rxv_cardListNotEmpty(engine, card))),

            rxv_cardListEmpty(engine, card)
                .filter(BooleanUtil::isTrue)
                .switchIfEmpty(engine.rxv_errorWithPageSource())
        ).all(ObjectUtil::nonNull).toFlowable();
    }

    /**
     * Reveal card list and delete all items for {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_revealCardList(Engine)
     * @see #rxa_dashboardCardTab(Engine, CardType)
     * @see #rxa_deleteAllCardItems(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_revealAndDeleteCardItems(@NotNull Engine<?> engine,
                                                     @NotNull CardType card) {
        return Flowable.concatArray(
            rxa_revealCardList(engine),
            rxa_dashboardCardTab(engine, card),
            rxa_deleteAllCardItems(engine, card)
        ).all(ObjectUtil::nonNull).toFlowable();
    }
}
