package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.param.DirectionParam;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.direction.Direction;
import org.swiften.xtestkitcomponents.direction.DirectionContainerType;

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
     * Click the add card button to open the card-adding menu.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openCardAddMenu(@NotNull final Engine<?> ENGINE) {
        return rxe_addCard(ENGINE).flatMap(ENGINE::rxa_click);
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
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxa_navigateBackOnce()
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #generalDelay(Engine)
     * @see #rxe_addCard(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_dismissDashboardTutorial(@NotNull final Engine<?> ENGINE) {
        final DashboardActionType THIS = this;

        if (ENGINE instanceof AndroidEngine) {
            return ENGINE
                .rxe_containsText("dashboard_title_tapToMakeFirstEntry")
                .flatMap(a -> ENGINE.rxa_navigateBackOnce())
                .map(BooleanUtil::toTrue)
                .onErrorReturnItem(true);
        } else if (ENGINE instanceof IOSEngine) {
            return Flowable.range(0, 2)
                .concatMap(a -> THIS.rxe_addCard(ENGINE))
                .flatMap(ENGINE::rxa_click)
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)
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
     * @see Engine#rxa_swipeGeneric(WebElement, DirectionContainerType)
     * @see DirectionParam.Builder#withDirection(Direction)
     * @see DirectionParam.Builder#withDuration(int)
     * @see DirectionParam.Builder#withEndRatio(double)
     * @see DirectionParam.Builder#withStartRatio(double)
     * @see DirectionParam.Builder#withTimes(int)
     * @see #dashboardModeSwitcherDuration(Engine)
     * @see #rxe_dashboardModeSwitcher(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dashboardMode(@NotNull final Engine<?> ENGINE,
                                          @NotNull DashboardMode mode) {
        final DirectionParam PARAM = DirectionParam.builder()
            .withDirection(mode.swipeDirection())
            .withDuration(dashboardModeSwitcherDuration(ENGINE))
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return rxe_dashboardModeSwitcher(ENGINE)
            .flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }
}
