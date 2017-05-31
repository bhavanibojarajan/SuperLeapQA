package com.holmusk.SuperLeapQA.ui.mealpage;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 31/5/17.
 */
public interface MealPageActionType extends MealPageValidationType {
    /**
     * Toggle meal edit mode.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_editMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openEditMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_editMeal(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Delete the current meal.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_deleteMeal(Engine)
     * @see #rxe_deleteMealConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_deleteMeal(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .concat(rxe_deleteMeal(ENGINE), rxe_deleteMealConfirm(ENGINE))
            .flatMap(ENGINE::rxa_click)
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}