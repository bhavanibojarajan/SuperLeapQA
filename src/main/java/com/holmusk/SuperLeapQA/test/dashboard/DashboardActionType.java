package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.HPReactives;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.swipe.SwipeParam;
import org.swiften.xtestkit.base.param.DirectionParam;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.coordinate.RLPoint;
import org.swiften.xtestkitcomponents.direction.Direction;
import org.swiften.xtestkitcomponents.direction.DirectionProviderType;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardActionType extends BaseActionType, DashboardValidationType {
    /**
     * Dismiss the tracker change popup. We declare "Not now" by default.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxa_dismissTrackerPopup(@NotNull final Engine<?> engine) {
        return engine
            .rxe_containsText("dashboard_title_notNow")
            .firstElement()
            .toFlowable()
            .compose(engine.clickFn())
            .map(HPBooleans::toTrue)
            .onErrorReturnItem(true);
    }

    /**
     * Click the Use App Now button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_useAppNow(Engine)
     */
    @NotNull
    default Flowable<?> rxa_useAppNow(@NotNull Engine<?> engine) {
        return rxe_useAppNow(engine).compose(engine.clickFn());
    }

    /**
     * Switch tab to one corresponding to {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_dashboardCardTab(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_dashboardCardTab(@NotNull final Engine<?> engine,
                                             @NotNull CardType card) {
        return rxe_dashboardCardTab(engine, card)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Reveal the card add {@link WebElement} if it is not visible. This is
     * only applicable to {@link Platform#ANDROID} since the FAB disappears
     * after a period of inactivity.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_swipeThenReverse(DirectionProviderType)
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    default Flowable<?> rxa_revealCardAdd(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_addCard(ENGINE)
                .onErrorResumeNext(Flowable
                    .<DirectionParam.Builder>create(obs -> {
                        DirectionParam.Builder param = DirectionParam.builder()
                            .withStartRatio(0.8d)
                            .withEndRatio(0.9d)
                            .withAnchorRLPosition(RLPoint.MID)
                            .withTimes(1)
                            .withDirection(Direction.DOWN_UP);

                        obs.onNext(param);
                        obs.onComplete();
                    }, BackpressureStrategy.BUFFER)
                    .map(DirectionParam.Builder::build)
                    .flatMap(ENGINE::rxa_swipeGeneric));
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Click the add card button to open
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxa_revealCardAdd(Engine)
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openCardAddMenu(@NotNull Engine<?> engine) {
        return Flowable
            .concatArray(
                rxa_revealCardAdd(engine),
                rxe_addCard(engine).compose(engine.clickFn())
            )
            .all(HPObjects::nonNull).toFlowable()
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Dismiss {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissCardAddMenu(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return ENGINE.rxa_navigateBackOnce();
        } else if (ENGINE instanceof IOSEngine) {
            return ENGINE.rxe_window()
                .map(WebElement::getLocation)
                .map(a -> a.moveBy(20, 20))
                .compose(ENGINE.tapPointFn())
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Navigate to the screen associated with a {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_dismissCardIntro(Engine)
     * @see #rxe_cardSelector(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_addCard(@NotNull Engine<?> engine,
                                    @NotNull CardType card) {
        return Flowable.concatArray(
            rxe_cardSelector(engine, card).compose(engine.clickFn()),
            rxa_dismissCardIntro(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Dismiss the card intro popup, if it is present on the screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_cardIntroDismiss(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissCardIntro(@NotNull Engine<?> engine) {
        return rxe_cardIntroDismiss(engine)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS)
            .map(HPBooleans::toTrue)
            .onErrorReturnItem(false);
    }

    /**
     * Dismiss {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}.
     * On {@link Platform#ANDROID}, we check whether there is some
     * {@link WebElement} that belongs to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}.
     * If yes, we navigate back once, otherwise do nothing. This is because
     * the tutorial status depends on values saved in SharedPreferences -
     * sometimes it is present, sometimes not.
     * On {@link Platform#IOS}, this is done by clicking on the add card button
     * twice. If this is not the first time the user is using the app, all
     * this does is simply opening up the menu then closing it.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxa_openCardAddMenu(Engine)
     * @see #rxa_dismissCardAddMenu(Engine)
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_dismissDashboardTutorial(@NotNull final Engine<?> E) {
        final DashboardActionType THIS = this;

        if (E instanceof AndroidEngine) {
            return E.rxe_containsText("dashboard_title_tapToMakeFirstEntry")
                .flatMap(a -> THIS.rxa_dismissCardAddMenu(E))
                .map(HPBooleans::toTrue)
                .onErrorReturnItem(true);
        } else if (E instanceof IOSEngine) {
            return HPReactives
                .concatDelayEach(
                    generalDelay(E),
                    rxa_openCardAddMenu(E).ofType(Object.class),
                    rxa_dismissCardAddMenu(E).ofType(Object.class)
                )
                .all(HPObjects::nonNull)
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the search menu from
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_dashboardSearch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleDashboardSearch(@NotNull Engine<?> engine) {
        return rxe_dashboardSearch(engine).compose(engine.clickFn());
    }

    /**
     * Switch dashboard mode by swiping on the switcher view.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link DashboardMode} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_dashboardModeSwitcher(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dashboardMode(@NotNull final Engine<?> ENGINE,
                                          @NotNull DashboardMode mode) {
        final DirectionParam PARAM = DirectionParam.builder()
            .withDirection(mode.swipeDirection())
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return rxe_dashboardModeSwitcher(ENGINE)
            .flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM))
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Swipe upwards to hide the dashboard mode switcher and review the card
     * list view.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_dashboardCardTab(Engine, CardType)
     * @see #rxe_dashboardModeSwitcher(Engine)
     */
    @NotNull
    default Flowable<?> rxa_revealCardList(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return Flowable.zip(
                rxe_dashboardModeSwitcher(ENGINE)
                    .map(a -> ENGINE.coordinate(a, RLPoint.MID, RLPoint.MIN))
                    .map(a -> a.moveBy(0, 10)),

                rxe_dashboardCardTab(ENGINE, CardType.ALL)
                    .map(a -> ENGINE.coordinate(a, RLPoint.MID, RLPoint.MAX))
                    .map(a -> a.moveBy(0, -10)),

                (a, b) -> SwipeParam.builder()
                    .withStartY(b.getY())
                    .withEndY(a.getY())
                    .withStartX(a.getX())
                    .withEndX(a.getX())
                    .withTimes(1)
                    .build())
                .flatMap(ENGINE::rxa_swipe)
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Navigate to the first content page corresponding to the first
     * {@link CardType} item.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_firstCardItem(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_firstCardItem(@NotNull Engine<?> engine,
                                          @NotNull CardType card) {
        return rxe_firstCardItem(engine, card)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }
}
