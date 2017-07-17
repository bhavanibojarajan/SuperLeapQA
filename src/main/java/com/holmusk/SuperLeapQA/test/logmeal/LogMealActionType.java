package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.photopicker.PhotoPickerActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.number.HPNumbers;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.util.HPLog;
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
     * @param engine {@link Engine} instance.
     * @param mood {@link Mood} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_mood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectMood(@NotNull Engine<?> engine,
                                       @NotNull Mood mood) {
        HPLog.printft("Selecting mood %s", mood);

        if (engine instanceof AndroidEngine) {
            return rxe_mood(engine, mood).compose(engine.clickFn());
        } else if (engine instanceof IOSEngine) {
            return rxe_mood(engine, mood).compose(engine.tapMiddleFn());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Select a random {@link Mood} from all available {@link Mood}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_selectMood(Engine, Mood)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomMood(@NotNull Engine<?> engine) {
        Mood mood = HPIterables.randomElement(Mood.values());
        return rxa_selectMood(engine, mood);
    }

    /**
     * Open {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * from {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_ENTRY}.
     * @param engine {@link Engine} instance.
     * @param index {@link Integer} value representing the pick index.
     * @return {@link Flowable} instance.
     * @see #rxe_photoPicker(Engine, int)
     */
    @NotNull
    default Flowable<?> rxa_openPhotoPicker(@NotNull Engine<?> engine, int index) {
        return rxe_photoPicker(engine, index).compose(engine.clickFn());
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
     * @see #rxa_openPhotoPicker(Engine, int)
     * @see #rxa_selectLibraryPhotos(Engine, int)
     * @see #rxa_confirmPhoto(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectMealPhotos(@NotNull final Engine<?> ENGINE) {
        final LogMealActionType THIS = this;
        final int COUNT = HPNumbers.randomBetween(1, Config.MAX_PHOTO_COUNT);

        if (ENGINE instanceof AndroidEngine) {
            return Flowable.range(0, COUNT)
                .map(a -> a + 1)
                .concatMap(a -> Flowable.concatArray(
                    THIS.rxa_openPhotoPicker(ENGINE, a),
                    THIS.rxa_selectLibraryPhotos(ENGINE, 1),
                    THIS.rxa_confirmPhoto(ENGINE)
                ).all(HPObjects::nonNull).toFlowable());
        } else if (ENGINE instanceof IOSEngine) {
            return Flowable
                .concatArray(
                    rxa_openPhotoPicker(ENGINE, 0),
                    rxa_selectLibraryPhotos(ENGINE, COUNT),
                    rxa_confirmPhoto(ENGINE)
                )
                .all(HPObjects::nonNull)
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Enable the meal description editable field by tapping on it. This is
     * only applicable on {@link Platform#IOS} because
     * {@link Engine#sendValue(WebElement, String)} does not work unless the
     * {@link WebElement} is already in focus.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<?> rxa_openMealDescription(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            TextInput input = TextInput.MEAL_DESCRIPTION;
            return rxe_editField(engine, input).compose(engine.tapMiddleFn());
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Confirm meal description. This is only relevant for {@link Platform#IOS}
     * because the meal description entry requires an Ok button to be pressed
     * to be confirmed.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxa_confirmMealDescription(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("mealLog_title_ok")
                .firstElement()
                .toFlowable()
                .compose(engine.clickFn());
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Open the meal time picker.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_mealTime(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openMealTimePicker(@NotNull Engine<?> engine) {
        /* We delay by some time for the time picker to fully appear */
        return rxe_mealTime(engine)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
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
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_mealTimeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmMealTime(@NotNull Engine<?> engine) {
        return rxe_mealTimeConfirm(engine)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Toggle location on or off.
     * @param engine {@link Engine} instance.
     * @param ON {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #rxe_mealLocSwitch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleMealLoc(@NotNull Engine<?> engine, boolean ON) {
        return rxe_mealLocSwitch(engine).compose(engine.toggleSwitchFn(ON));
    }

    /**
     * Submit the current meal log.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #cssLogProgressDelay(Engine)
     * @see #rxe_mealConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitMeal(@NotNull final Engine<?> engine) {
        return rxe_mealConfirm(engine)
            .compose(engine.clickFn())
            .delay(cssLogProgressDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Log a new meal with random information.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #randomSelectableTime()
     * @see #rxa_selectMealPhotos(Engine)
     * @see #rxa_selectRandomMood(Engine)
     * @see #rxa_openMealDescription(Engine)
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
                rxa_openMealDescription(engine),
                rxa_randomInput(engine, TextInput.MEAL_DESCRIPTION),
                rxa_confirmMealDescription(engine),
//                rxa_selectMealPhotos(ENGINE),
//                rxa_openMealTimePicker(ENGINE),
//                rxa_selectMealTime(ENGINE, TIME),
//                rxa_confirmMealTime(ENGINE),
                rxa_submitMeal(engine)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }
}
