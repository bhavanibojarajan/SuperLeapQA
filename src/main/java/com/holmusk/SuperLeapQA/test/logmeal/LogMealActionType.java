package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.photopicker.PhotoPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/30/17.
 */
public interface LogMealActionType extends LogMealValidationType, PhotoPickerActionType {
    /**
     * Select a {@link Mood}.
     * @param ENGINE {@link Engine} instance.
     * @param mood {@link Mood} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_mood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectMood(@NotNull final Engine<?> ENGINE,
                                       @NotNull Mood mood) {
        LogUtil.printft("Selecting mood %s", mood);
        return rxe_mood(ENGINE, mood).flatMap(ENGINE::rxa_click);
    }

    /**
     * Select a random {@link Mood} from all available {@link Mood}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see CollectionUtil#randomElement(Object[])
     * @see Mood#values()
     * @see #rxa_selectMood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomMood(@NotNull Engine<?> engine) {
        Mood mood = CollectionUtil.randomElement(Mood.values());
        return rxa_selectMood(engine, mood);
    }

    /**
     * Open {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * from {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_ENTRY}.
     * @param ENGINE {@link Engine} instance.
     * @param index {@link Integer} value representing the pick index.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_photoPicker(Engine, int)
     */
    @NotNull
    default Flowable<?> rxa_openPhotoPicker(@NotNull final Engine<?> ENGINE, int index) {
        return rxe_photoPicker(ENGINE, index).flatMap(ENGINE::rxa_click);
    }

    /**
     * Select a number of photos for a meal log. The maximum number of photos
     * to be selected is specified by {@link Config#MAX_PHOTO_COUNT}.
     * On {@link Platform#ANDROID}, we need to
     * select the photos one by one (i.e. click on each photo placeholder to
     * open up the photo picker, select 1 photo then confirm).
     * On {@link Platform#ANDROID}, we can simply
     * go to the picker and select multiple photos at once.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Config#MAX_PHOTO_COUNT
     * @see NumberUtil#randomBetween(int, int)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_openPhotoPicker(Engine, int)
     * @see #rxa_selectLibraryPhotos(Engine, int)
     * @see #rxa_confirmPhoto(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectMealPhotos(@NotNull final Engine<?> ENGINE) {
        final LogMealActionType THIS = this;
        final int COUNT = NumberUtil.randomBetween(1, Config.MAX_PHOTO_COUNT);

        if (ENGINE instanceof AndroidEngine) {
            return Flowable.range(0, COUNT)
                .map(a -> a + 1)
                .concatMap(a -> Flowable.concatArray(
                    THIS.rxa_openPhotoPicker(ENGINE, a),
                    THIS.rxa_selectLibraryPhotos(ENGINE, 1),
                    THIS.rxa_confirmPhoto(ENGINE)
                ).all(ObjectUtil::nonNull).toFlowable());
        } else if (ENGINE instanceof IOSEngine) {
            return Flowable
                .concatArray(
                    rxa_openPhotoPicker(ENGINE, 0),
                    rxa_selectLibraryPhotos(ENGINE, COUNT),
                    rxa_confirmPhoto(ENGINE)
                )
                .all(ObjectUtil::nonNull)
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Confirm meal description. This is only relevant for {@link Platform#IOS}
     * because the meal description entry requires an Ok button to be pressed
     * to be confirmed.
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
     * Open the meal time picker.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxe_mealTime(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openMealTimePicker(@NotNull final Engine<?> ENGINE) {
        /* We delay by some time for the time picker to fully appear */
        return rxe_mealTime(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Select a meal time with a {@link Date} instance.
     * @param engine {@link Engine} instance.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_selectSpinnerDateTime(Engine, Date)
     */
    @NotNull
    default Flowable<?> rxa_selectMealTime(@NotNull Engine<?> engine,
                                           @NotNull Date date) {
        return rxa_selectSpinnerDateTime(engine, date);
    }

    /**
     * Select a random meal time.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #randomSelectableTime()
     * @see #rxa_selectMealTime(Engine, Date)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomMealTime(@NotNull Engine<?> engine) {
        Date time = randomSelectableTime();
        return rxa_selectMealTime(engine, time);
    }

    /**
     * Confirm meal time selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #generalDelay(Engine)
     * @see #rxe_mealTimeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmMealTime(@NotNull final Engine<?> ENGINE) {
        return rxe_mealTimeConfirm(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Toggle location on or off.
     * @param ENGINE {@link Engine} instance.
     * @param ON {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_toggleSwitch(WebElement, boolean)
     * @see #rxe_mealLocSwitch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleMealLocation(@NotNull final Engine<?> ENGINE,
                                               final boolean ON) {
        return rxe_mealLocSwitch(ENGINE).flatMap(a -> ENGINE.rxa_toggleSwitch(a, ON));
    }

    /**
     * Submit the current meal log.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #CSSLogProgressDelay(Engine)
     * @see #rxe_mealConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_mealConfirm(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(CSSLogProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Log a new meal with random information.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#MEAL_DESCRIPTION
     * @see #randomSelectableTime()
     * @see #rxa_selectMealPhotos(Engine)
     * @see #rxa_selectRandomMood(Engine)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_confirmMealDescription(Engine)
     * @see #rxa_openMealTimePicker(Engine)
     * @see #rxa_selectMealTime(Engine, Date)
     * @see #rxa_confirmMealTime(Engine)
     * @see #rxa_submitMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxa_logNewMeal(@NotNull Engine<?> engine) {
        final Date TIME = randomSelectableTime();

        return Flowable
            .concatArray(
                rxa_randomInput(engine, TextInput.MEAL_DESCRIPTION),
                rxa_confirmMealDescription(engine),
//                rxa_selectMealPhotos(ENGINE),
//                rxa_openMealTimePicker(ENGINE),
//                rxa_selectMealTime(ENGINE, TIME),
//                rxa_confirmMealTime(ENGINE),
                rxa_submitMeal(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
