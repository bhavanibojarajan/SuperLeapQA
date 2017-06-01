package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DashboardMode;
import com.holmusk.SuperLeapQA.ui.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.ui.personalinfo.PersonalInfoActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.param.UnidirectionParam;
import org.swiften.xtestkit.base.type.DurationType;
import org.swiften.xtestkit.base.type.RepeatType;

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
     * Dismiss the dashboard tutorial by click on the add card button twice.
     * If this is not the first time the user is using the app, all this does
     * is simply opening up the menu then closing it.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement, RepeatType)
     * @see #generalDelay()
     * @see #rxe_addCard(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissDashboardTutorial(@NotNull final Engine<?> ENGINE) {
        return rxe_addCard(ENGINE)
            .flatMap(a -> ENGINE.rxa_click(a, () -> 2))
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
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
     * @see #dashboardModeSwitcherDuration()
     * @see #rxe_dashboardModeSwitcher(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dashboardMode(@NotNull final Engine<?> ENGINE,
                                          @NotNull DashboardMode mode) {
        final UnidirectionParam PARAM = UnidirectionParam.builder()
            .withDirection(mode.swipeDirection())
            .withDuration(dashboardModeSwitcherDuration())
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return rxe_dashboardModeSwitcher(ENGINE)
            .flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }
}
