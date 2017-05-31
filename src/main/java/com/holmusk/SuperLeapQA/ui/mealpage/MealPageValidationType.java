package com.holmusk.SuperLeapQA.ui.mealpage;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haipham on 31/5/17.
 */
public interface MealPageValidationType extends BaseValidationType {
    /**
     * Get the {@link org.openqa.selenium.WebElement} that is displaying the
     * meal time and check that it is displaying the correct time.
     * @param engine {@link Engine} instance.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxv_hasMealTime(@NotNull Engine<?> engine, @NotNull Date date) {
        String format = "M/d/YY, h:mm a";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return engine.rxe_containsText(dateString).firstElement().toFlowable();
    }
}
