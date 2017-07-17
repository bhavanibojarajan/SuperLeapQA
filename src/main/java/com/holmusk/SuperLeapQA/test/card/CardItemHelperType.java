package com.holmusk.SuperLeapQA.test.card;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.navigation.type.ScreenInitType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.HPReactives;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 6/20/17.
 */
public interface CardItemHelperType extends DashboardActionType, ScreenInitType {
    /**
     * Complete steps to delete the first item corresponding to {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_firstCardItem(Engine, CardType)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxn_cardItemPageInitialized(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_deleteFirstCardItem(@NotNull Engine<?> engine,
                                                @NotNull CardType card) {
        return Flowable.concatArray(
            rxa_firstCardItem(engine, card),
            rxn_cardItemPageInitialized(engine, card),
            rxa_openEditMenu(engine),
            rxa_deleteFromMenu(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Delete all items for {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see HPReactives#doWhile(Flowable, Flowable, Object)
     * @see #rxa_deleteFirstCardItem(Engine, CardType)
     * @see #rxv_cardListEmpty(Engine, CardType)
     * @see #rxv_cardListNotEmpty(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_deleteAllCardItems(@NotNull Engine<?> engine,
                                               @NotNull CardType card) {
        return Flowable.concatArray(
            HPReactives.doWhile(
                rxa_deleteFirstCardItem(engine, card),
                rxv_cardListNotEmpty(engine, card)
            ),

            rxv_cardListEmpty(engine, card)
                .filter(HPBooleans::isTrue)
                .switchIfEmpty(engine.rxv_error())
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Reveal card list and delete all items for {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
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
        ).all(HPObjects::nonNull).toFlowable();
    }
}
