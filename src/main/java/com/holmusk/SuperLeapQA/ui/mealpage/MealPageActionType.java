package com.holmusk.SuperLeapQA.ui.mealpage;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.general.Unidirection;
import org.swiften.xtestkit.base.param.UnidirectionParam;
import org.swiften.xtestkit.base.type.DurationType;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 31/5/17.
 */
public interface MealPageActionType extends MealPageValidationType {
    /**
     * Toggle meal edit mode, then delay for a while for the menu to fully
     * appear.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay()
     * @see #rxe_editMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openEditMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_editMeal(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Delete the current meal.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see ObjectUtil#nonNull(Object)
     * @see #mealDeleteProgressDelay()
     * @see #rxe_deleteMeal(Engine)
     * @see #rxe_deleteMealConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_deleteMeal(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .concat(rxe_deleteMeal(ENGINE), rxe_deleteMealConfirm(ENGINE))
            .flatMap(ENGINE::rxa_click)
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .delay(mealDeleteProgressDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Navigate back to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_dashboardBack(Engine)
     */
    @NotNull
    default Flowable<?> rxa_backToDashboard(@NotNull final Engine<?> ENGINE) {
        return rxe_dashboardBack(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * When we enter {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * from {@link com.holmusk.SuperLeapQA.navigation.Screen#SEARCH} by
     * searching for a comment, the scroll view will scroll to bottom. We need
     * to scroll up to reveal the edit button so that the {@link WebElement}
     * from {@link #rxe_editMeal(Engine)} can be clicked.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_swipeGeneric(WebElement, DurationType)
     * @see Engine#rxe_window()
     * @see Unidirection#UP_DOWN
     * @see UnidirectionParam.Builder#withDirection(Unidirection)
     * @see UnidirectionParam.Builder#withStartRatio(double)
     * @see UnidirectionParam.Builder#withEndRatio(double)
     * @see #rxe_mealImage(Engine)
     */
    @NotNull
    default Flowable<?> rxa_makeEditButtonVisible(@NotNull final Engine<?> ENGINE) {
        final UnidirectionParam PARAM = UnidirectionParam.builder()
            .withDirection(Unidirection.UP_DOWN)
            .withStartRatio(0.3d)
            .withEndRatio(0.7d)
            .build();

        return ENGINE.rxe_window().flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }
}
