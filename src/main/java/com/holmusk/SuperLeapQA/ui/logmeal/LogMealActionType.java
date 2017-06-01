package com.holmusk.SuperLeapQA.ui.logmeal;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLTextType;
import com.holmusk.SuperLeapQA.ui.photopicker.PhotoPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.date.CalendarUnit;
import org.swiften.xtestkit.base.element.date.DateParam;
import org.swiften.xtestkit.base.element.date.DatePickerType;
import org.swiften.xtestkit.base.element.date.DateType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.element.date.IOSDatePickerType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/30/17.
 */
public interface LogMealActionType extends LogMealValidationType, PhotoPickerActionType {
    /**
     * Open {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * from {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_photoPicker(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openPhotoPicker(@NotNull final Engine<?> ENGINE) {
        return rxe_photoPicker(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Open the meal time picker.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay()
     * @see #rxe_mealTime(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openMealTimePicker(@NotNull final Engine<?> ENGINE) {
        /* We delay by some time for the time picker to fully appear */
        return rxe_mealTime(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Select a {@link Mood}.
     * @param ENGINE {@link Engine} instance.
     * @param mood {@link Mood} instance.
     * @return {@link Flowable} instance.
     * @see Engine#getMiddleCoordinate(WebElement)
     * @see Engine#rxa_click(WebElement)
     * @see WebElement#getSize()
     * @see #rxe_mood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectMood(@NotNull final Engine<?> ENGINE, @NotNull Mood mood) {
        LogUtil.printfThread("Selecting mood %s", mood);
        return rxe_mood(ENGINE, mood).flatMap(ENGINE::rxa_click);
    }

    /**
     * Select a random {@link Mood} from all available {@link Mood}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see CollectionTestUtil#randomElement(Object[])
     * @see Mood#values()
     * @see #rxa_selectMood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomMood(@NotNull Engine<?> engine) {
        Mood mood = CollectionTestUtil.randomElement(Mood.values());
        return rxa_selectMood(engine, mood);
    }

    /**
     * Toggle location on or off.
     * @param ENGINE {@link Engine} instance.
     * @param ON {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_toggleSwitch(WebElement, boolean)
     * @see #rxe_locationSwitch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleLocation(@NotNull final Engine<?> ENGINE, final boolean ON) {
        return rxe_locationSwitch(ENGINE).flatMap(a -> ENGINE.rxa_toggleSwitch(a, ON));
    }

    /**
     * Select a number of photos for a meal log. The maximum number of photos
     * to be selected is specified by {@link Config#MAX_PHOTO_COUNT}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Config#MAX_PHOTO_COUNT
     * @see #rxa_openPhotoPicker(Engine)
     * @see #rxa_selectLibraryPhotos(Engine, int)
     * @see #rxa_confirmPhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxa_selectMealPhotos(@NotNull final Engine<?> ENGINE) {
        final LogMealActionType THIS = this;
        final int COUNT = Config.MAX_PHOTO_COUNT;

        return rxa_openPhotoPicker(ENGINE)
            .flatMap(a -> THIS.rxa_selectLibraryPhotos(ENGINE, COUNT))
            .flatMap(a -> THIS.rxa_confirmPhoto(ENGINE));
    }

    /**
     * Confirm meal description. This is only relevant for
     * {@link org.swiften.xtestkit.mobile.Platform#IOS} because the meal
     * description entry requires an Ok button to be pressed to be confirmed.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxa_confirmMealDescription(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            return ENGINE
                .rxe_containsText("mealLog_title_ok")
                .firstElement()
                .toFlowable()
                .flatMap(ENGINE::rxa_click);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Select a meal time with a {@link Date} instance.
     * @param engine {@link Engine} instance.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see CalendarUnit#MONTH
     * @see CalendarUnit#DAY
     * @see CalendarUnit#HOUR
     * @see CalendarUnit#MINUTE
     * @see CalendarUnit#PERIOD
     * @see DateParam.Builder#withDate(Date)
     * @see DateParam.Builder#withPickerType(DatePickerType)
     * @see DateParam.Builder#withCalendarUnits(List)
     * @see Engine#rxa_selectDate(DateType)
     * @see IOSDatePickerType#MMMd_h_mm_a
     */
    @NotNull
    default Flowable<?> rxa_selectMealTime(@NotNull Engine<?> engine,
                                           @NotNull Date date) {
        DateParam.Builder builder = DateParam.builder().withDate(date);
        List<CalendarUnit> units;
        DatePickerType pickerType;

        if (engine instanceof IOSEngine) {
            units = Arrays.asList(
                CalendarUnit.MONTH,
                CalendarUnit.DAY,
                CalendarUnit.HOUR,
                CalendarUnit.MINUTE,
                CalendarUnit.PERIOD
            );

            pickerType = IOSDatePickerType.MMMd_h_mm_a;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        DateType param = builder
            .withCalendarUnits(units)
            .withPickerType(pickerType)
            .build();

        return engine.rxa_selectDate(param);
    }

    /**
     * Confirm meal time selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_mealTimeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmMealTime(@NotNull final Engine<?> ENGINE) {
        return rxe_mealTimeConfirm(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Submit the current meal log.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_mealConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_mealConfirm(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Log a new meal with random information.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see TextInput#MEAL_DESCRIPTION
     * @see #rxa_selectMealPhotos(Engine)
     * @see #rxa_selectRandomMood(Engine)
     * @see #rxa_randomInput(Engine, SLTextType)
     * @see #rxa_confirmMealDescription(Engine)
     * @see #rxa_openMealTimePicker(Engine)
     * @see #rxa_selectMealTime(Engine, Date)
     * @see #rxa_confirmMealTime(Engine)
     * @see #rxa_submitMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxa_logNewMeal(@NotNull final Engine<?> ENGINE) {
        final LogMealActionType THIS = this;

        return rxa_selectRandomMood(ENGINE)
            .flatMap(a -> THIS.rxa_selectMealPhotos(ENGINE))
            .flatMap(a -> THIS.rxa_openMealTimePicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectMealTime(ENGINE, new Date()))
            .flatMap(a -> THIS.rxa_confirmMealTime(ENGINE))
            .flatMap(a -> THIS.rxa_randomInput(ENGINE, TextInput.MEAL_DESCRIPTION))
            .flatMap(a -> THIS.rxa_confirmMealDescription(ENGINE))
            .flatMap(a -> THIS.rxa_submitMeal(ENGINE));
    }
}
