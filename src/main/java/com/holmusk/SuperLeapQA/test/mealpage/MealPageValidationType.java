package com.holmusk.SuperLeapQA.test.mealpage;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.date.DateUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.javautilities.protocol.ClassNameType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * @see Engine#localizer()
     * @see Engine#rxe_containsText(String...)
     * @see LocalizerType#localize(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxv_hasMealTime(@NotNull Engine<?> engine, @NotNull Date date) {
        String dateString;

        if (engine instanceof AndroidEngine) {
            LocalizerType localizer = engine.localizer();
            Date current = new Date();
            SimpleDateFormat hmaFormatter = new SimpleDateFormat("h:mm a");
            String hma = hmaFormatter.format(date);

            if (DateUtil.sameAs(date, current, Calendar.DAY_OF_MONTH)) {
                String today = localizer.localize("date_title_today");
                dateString = String.format("%s %s", today, hma);
            } else if (DateUtil.difference(current, date, Calendar.DAY_OF_MONTH) == 1) {
                String yesterday = localizer.localize("date_title_yesterday");
                dateString = String.format("%s %s", yesterday, hma);
            } else {
                SimpleDateFormat dmyFormatter = new SimpleDateFormat("dd MMM yyyy");
                String dmy = dmyFormatter.format(date);
                dateString = String.format("%s %s", dmy, hma);
            }
        } else if (engine instanceof IOSEngine) {
            String format = "M/d/YY, h:mm a";
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            dateString = formatter.format(date);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        return engine.rxe_containsText(dateString).firstElement().toFlowable();
    }

    /**
     * Get the meal image {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see AndroidView.Type#IMAGE_VIEW
     * @see IOSView.Type#UI_IMAGE_VIEW
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_mealImage(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.Type.IMAGE_VIEW)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.Type.UI_IMAGE_VIEW)
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
