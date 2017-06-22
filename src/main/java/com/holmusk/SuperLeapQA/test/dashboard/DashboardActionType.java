package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.swipe.SwipeParam;
import org.swiften.xtestkit.base.param.DirectionParam;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.javautilities.protocol.RepeatType;
import org.swiften.xtestkitcomponents.coordinate.RLPoint;
import org.swiften.xtestkitcomponents.coordinate.RLPositionType;
import org.swiften.xtestkitcomponents.direction.Direction;
import org.swiften.xtestkitcomponents.direction.DirectionProviderType;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardActionType extends BaseActionType, DashboardValidationType {
    /**
     * Dismiss the tracker change popup. We declare "Not now" by default.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxa_dismissTrackerPopup(@NotNull final Engine<?> ENGINE) {
        return ENGINE
            .rxe_containsText("dashboard_title_notNow")
            .firstElement()
            .toFlowable()
            .flatMap(ENGINE::rxa_click)
            .map(BooleanUtil::toTrue)
            .onErrorReturnItem(true);
    }

    /**
     * Click the Use App Now button.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_useAppNow(Engine)
     */
    @NotNull
    default Flowable<?> rxa_useAppNow(@NotNull final Engine<?> ENGINE) {
        return rxe_useAppNow(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Switch tab to one corresponding to {@link CardType}.
     * @param ENGINE {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxe_dashboardCardTab(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_dashboardCardTab(@NotNull final Engine<?> ENGINE,
                                             @NotNull CardType card) {
        return rxe_dashboardCardTab(ENGINE, card)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Reveal the card add {@link WebElement} if it is not visible. This is
     * only applicable to {@link Platform#ANDROID} since the FAB disappears
     * after a period of inactivity.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see DirectionParam.Builder#withAnchorRLPosition(RLPositionType)
     * @see DirectionParam.Builder#withDirection(Direction)
     * @see DirectionParam.Builder#withEndRatio(double)
     * @see DirectionParam.Builder#withStartRatio(double)
     * @see DirectionParam.Builder#withTimes(int)
     * @see Engine#rxa_swipeThenReverse(DirectionProviderType)
     * @see Direction#DOWN_UP
     * @see RLPoint#MID
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
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxa_revealCardAdd(Engine)
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openCardAddMenu(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .concatArray(
                rxa_revealCardAdd(ENGINE),
                rxe_addCard(ENGINE).flatMap(ENGINE::rxa_click)
            )
            .all(ObjectUtil::nonNull).toFlowable()
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Dismiss {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_navigateBackOnce()
     * @see Engine#rxa_tap(Point)
     * @see Engine#rxe_window()
     * @see org.openqa.selenium.Point#moveBy(int, int)
     * @see WebElement#getLocation()
     * @see #generalDelay(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_dismissCardAddMenu(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return ENGINE.rxa_navigateBackOnce();
        } else if (ENGINE instanceof IOSEngine) {
            return ENGINE.rxe_window()
                .map(WebElement::getLocation)
                .map(a -> a.moveBy(20, 20))
                .flatMap(ENGINE::rxa_tap)
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Navigate to the screen associated with a {@link CardType}.
     * @param ENGINE {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_cardSelector(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_addCard(@NotNull final Engine<?> ENGINE,
                                    @NotNull CardType card) {
        return rxe_cardSelector(ENGINE, card).flatMap(ENGINE::rxa_click);
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
     * @see ObjectUtil#nonNull(Object)
     * @see RxUtil#concatDelayEach(long, Flowable[])
     * @see #generalDelay(Engine)
     * @see #rxa_openCardAddMenu(Engine)
     * @see #rxa_dismissCardAddMenu(Engine)
     * @see #rxe_addCard(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_dismissDashboardTutorial(@NotNull final Engine<?> E) {
        final DashboardActionType THIS = this;

        if (E instanceof AndroidEngine) {
            return E.rxe_containsText("dashboard_title_tapToMakeFirstEntry")
                .flatMap(a -> THIS.rxa_dismissCardAddMenu(E))
                .map(BooleanUtil::toTrue)
                .onErrorReturnItem(true);
        } else if (E instanceof IOSEngine) {
            return RxUtil
                .concatDelayEach(
                    generalDelay(E),
                    rxa_openCardAddMenu(E).ofType(Object.class),
                    rxa_dismissCardAddMenu(E).ofType(Object.class)
                )
                .all(ObjectUtil::nonNull)
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the search menu from
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_dashboardSearch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleDashboardSearch(@NotNull final Engine<?> ENGINE) {
        return rxe_dashboardSearch(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Switch dashboard mode by swiping on the switcher view.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link DashboardMode} instance.
     * @return {@link Flowable} instance.
     * @see DashboardMode#swipeDirection()
     * @see Engine#rxa_swipeGeneric(WebElement, DirectionProviderType)
     * @see DirectionParam.Builder#withDirection(Direction)
     * @see DirectionParam.Builder#withDuration(int)
     * @see DirectionParam.Builder#withEndRatio(double)
     * @see DirectionParam.Builder#withStartRatio(double)
     * @see DirectionParam.Builder#withTimes(int)
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
     * @see Engine#coordinate(WebElement, RLPositionType, RLPositionType)
     * @see Engine#rxa_swipe(RepeatType)
     * @see Point#getY()
     * @see Point#moveBy(int, int)
     * @see SwipeParam.Builder#withStartX(int)
     * @see SwipeParam.Builder#withEndX(int)
     * @see SwipeParam.Builder#withStartY(int)
     * @see SwipeParam.Builder#withEndY(int)
     * @see SwipeParam.Builder#withTimes(int)
     * @see CardType#ALL
     * @see RLPoint#MIN
     * @see RLPoint#MID
     * @see RLPoint#MAX
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
     * @param ENGINE {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxe_firstCardItem(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxa_firstCardItem(@NotNull final Engine<?> ENGINE,
                                          @NotNull CardType card) {
        return rxe_firstCardItem(ENGINE, card)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
