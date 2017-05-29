package com.holmusk.SuperLeapQA.ui.logmeal;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 29/5/17.
 */
public interface LogMealValidationType {
    /**
     * Get the meal log cancel button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealCancel(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealLog_title_cancel")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the meal log confirm button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealConfirm(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealLog_title_confirm")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the meal description button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealDescription(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealLog_title_description")
            .firstElement()
            .toFlowable();
    }
}
