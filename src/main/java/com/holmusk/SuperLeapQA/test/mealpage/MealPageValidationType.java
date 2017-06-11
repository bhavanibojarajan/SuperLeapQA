package com.holmusk.SuperLeapQA.test.mealpage;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.date.DateUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haipham on 31/5/17.
 */
public interface MealPageValidationType extends BaseValidationType {
    /**
     * Get the image tutorial dismiss {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealImageTutorialDismiss(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealPage_title_gotIt")
            .firstElement()
            .toFlowable();
    }

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
                SimpleDateFormat dmyFormatter = new SimpleDateFormat("dd MMM YYYY");
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
     * Get the meal delete button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_deleteMeal(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealPage_title_delete")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the meal delete confirm button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_deleteMealConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("btn_dialog_alert_promopt_positive")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("mealPage_title_delete")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the button that we can click to get back to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attribute.Builder#addAttribute(String)
     * @see Attribute.Builder#withValue(Object)
     * @see Attribute.Builder#withFormatible(Attribute.Formatible)
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see Point#getX()
     * @see XPath.Builder#addAttribute(Attribute)
     * @see WebElement#getLocation()
     * @see IOSView.ViewType#UI_BUTTON
     * @see com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD
     * @see #rxe_backButton(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardBack(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_backButton(engine);
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_BUTTON.className())
                .filter(a -> a.getLocation().getX() == 0)
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the meal image {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see AndroidView.ViewType#IMAGE_VIEW
     * @see IOSView.ViewType#UI_IMAGE_VIEW
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_mealImage(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.ViewType.IMAGE_VIEW.className())
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_IMAGE_VIEW.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
