package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.personalinfo.UIPersonalInfoTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
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
        data.add(new Object[] { UserMode.PARENT, CardType.WEIGHT });
        return data.iterator();
    }

    /**
     * Delete all cards for a particular {@link CardType}.
     * @param mode {@link UserMode} instance.
     * @param card {@link CardType} instance.
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
     * @see #rxv_cardListNotEmpty(Engine, CardType)
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
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
