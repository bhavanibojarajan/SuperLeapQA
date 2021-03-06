package com.holmusk.SuperLeapQA.test.dob;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.element.date.AndroidDatePickerType;
import org.swiften.xtestkit.android.type.AndroidSDK;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.date.DateParam;
import org.swiften.xtestkit.base.element.date.DatePickerType;
import org.swiften.xtestkit.base.element.date.DateProviderType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.element.date.IOSDatePickerType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerActionType extends BaseActionType, DOBPickerValidationType {
    /**
     * Open the DoB dialog in the sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * Due to design differences, only
     * {@link Platform#ANDROID} requires the
     * DoB picker to be opened. On
     * {@link Platform#IOS}, the picker is visible
     * immediately upon entering the screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_DoBEditField(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openDoBPicker(@NotNull Engine<?> engine) {
        return rxe_DoBEditField(engine)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #generalDelay(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmDoB(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText(
                "register_title_ok",
                "register_title_submit",
                "register_title_done"
            )
            .firstElement()
            .toFlowable()
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param engine {@link Engine} instance.
     * @param DATE {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_selectDate(DateProviderType)
     */
    @NotNull
    default Flowable<?> rxa_selectDoB(@NotNull Engine<?> engine,
                                      @NotNull final Date DATE) {
        HPLog.printft("Selecting DoB %s", DATE);
        DatePickerType pickerType;

        if (engine instanceof AndroidEngine) {
            AndroidEngine aEngine = (AndroidEngine)engine;
            AndroidSDK sdk = aEngine.androidSDK();

            if (sdk.isAtLeastM()) {
                pickerType = AndroidDatePickerType.DATE_CALENDAR_PICKER_M;
            } else if (sdk.isAtLeastLollipop()) {
                pickerType = AndroidDatePickerType.DATE_CALENDAR_PICKER;
            } else {
                pickerType = AndroidDatePickerType.DATE_NUMBER_PICKER_MMM_dd_yyyy;
            }
        } else if (engine instanceof IOSEngine) {
            pickerType = IOSDatePickerType.PICKER_WHEEL_MMMM_d_yyyy;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        return engine.rxa_selectDate(DateParam.builder()
            .withDate(DATE)
            .withDatePickerUnits()
            .withPicker(pickerType)
            .build());
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param engine {@link Engine} instance.
     * @param age {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rxa_selectDoB(Engine, Date)
     */
    @NotNull
    default Flowable<?> rxa_selectDoBToBeOfAge(@NotNull Engine<?> engine, int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return rxa_selectDoB(engine, calendar.getTime());
    }
}
