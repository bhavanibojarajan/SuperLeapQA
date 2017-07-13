package com.holmusk.SuperLeapQA.test.mealpage;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import com.holmusk.SuperLeapQA.test.search.SearchActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.param.DirectionParam;
import org.swiften.xtestkitcomponents.direction.Direction;

/**
 * Created by haipham on 31/5/17.
 */
public interface MealPageActionType extends
    BaseActionType,
    MealPageValidationType,
    SearchActionType
{
    /**
     * Dismiss the meal image tutorial. However, if this is not the first time
     * the user is logging a meal and there is no such tutorial, simply swallow
     * the error.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_mealImageTutDismiss(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissMealImageTutorial(@NotNull final Engine<?> E) {
        return rxe_mealImageTutDismiss(E)
            .flatMap(E::rxa_click)
            .map(HPBooleans::toTrue)
            .onErrorReturnItem(true);
    }

    /**
     * When we enter {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * from {@link com.holmusk.SuperLeapQA.navigation.Screen#SEARCH} by
     * searching for a comment, the scroll view will scroll to bottom. We need
     * to scroll up to reveal the edit button so that the {@link WebElement}
     * from {@link #rxe_editToggle(Engine)} can be clicked.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_mealImage(Engine)
     */
    @NotNull
    default Flowable<?> rxa_makeEditButtonVisible(@NotNull final Engine<?> ENGINE) {
        final DirectionParam PARAM = DirectionParam.builder()
            .withDirection(Direction.UP_DOWN)
            .withStartRatio(0.3d)
            .withEndRatio(0.7d)
            .build();

        return ENGINE.rxe_window().flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }

    /**
     * Navigate back to {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * after searching for and deleting a meal.
     * This is only applicable to {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * whereby {@link com.holmusk.SuperLeapQA.navigation.Screen#SEARCH} is still
     * visible after {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * is dismissed.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_cancelSearch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dashboardAfterSearchAndDelete(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxa_cancelSearch(engine);
        } else {
            return Flowable.just(true);
        }
    }
}
