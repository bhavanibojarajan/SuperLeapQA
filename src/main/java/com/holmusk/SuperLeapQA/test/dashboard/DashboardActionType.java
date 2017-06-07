package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.test.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.test.personalinfo.PersonalInfoActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.param.UnidirectionParam;
import org.swiften.xtestkitcomponents.common.DurationType;
import org.swiften.xtestkitcomponents.common.RepeatType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.direction.Unidirection;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardActionType extends
    PersonalInfoActionType,
    InvalidAgeActionType,
    DashboardValidationType
{
    /**
     * Click the Use App Now button.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_useAppNow(Engine)
     * @see Engine#rxa_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
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
     * Dismiss the dashboard tutorial.
     * On {@link Platform#ANDROID}, this is done
     * by navigating back once.
     * On {@link Platform#IOS}, this is done by
     * clicking on the add card button twice. If this is not the first time
     * the user is using the app, all this does is simply opening up the menu
     * then closing it.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement, RepeatType)
     * @see Engine#rxa_navigateBackOnce()
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #generalDelay(Engine)
     * @see #rxe_addCard(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_dismissDashboardTutorial(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return ENGINE.rxa_navigateBackOnce();
        } else if (ENGINE instanceof IOSEngine) {
            return rxe_addCard(ENGINE)
                .flatMap(a -> ENGINE.rxa_click(a, () -> 2))
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
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
     * @see Engine#rxa_swipeGeneric(WebElement, DurationType)
     * @see UnidirectionParam.Builder#withDirection(Unidirection)
     * @see UnidirectionParam.Builder#withDuration(int)
     * @see UnidirectionParam.Builder#withEndRatio(double)
     * @see UnidirectionParam.Builder#withStartRatio(double)
     * @see UnidirectionParam.Builder#withTimes(int)
     * @see #dashboardModeSwitcherDuration(Engine)
     * @see #rxe_dashboardModeSwitcher(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dashboardMode(@NotNull final Engine<?> ENGINE,
                                          @NotNull DashboardMode mode) {
        final UnidirectionParam PARAM = UnidirectionParam.builder()
            .withDirection(mode.swipeDirection())
            .withDuration(dashboardModeSwitcherDuration(ENGINE))
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return rxe_dashboardModeSwitcher(ENGINE)
            .flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }

    /**
     * Scroll to the bottom of the screen to validate cards.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_swipeGeneric(WebElement, DurationType)
     * @see Engine#rxe_window()
     * @see Unidirection#DOWN_UP
     * @see UnidirectionParam.Builder#withDirection(Unidirection)
     * @see UnidirectionParam.Builder#withDuration(int)
     * @see UnidirectionParam.Builder#withEndRatio(double)
     * @see UnidirectionParam.Builder#withStartRatio(double)
     * @see UnidirectionParam.Builder#withTimes(int)
     */
    @NotNull
    default Flowable<?> rxa_scrollToBottom(@NotNull final Engine<?> E) {
        final UnidirectionParam PARAM = UnidirectionParam.builder()
            .withDirection(Unidirection.DOWN_UP)
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return E.rxe_window().flatMap(a -> E.rxa_swipeGeneric(a, PARAM));
    }
}
