package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollActionType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 9/6/17.
 */
public interface LogWeightActionType extends
    BaseActionType,
    HMCircleScrollActionType,
    LogWeightValidationType
{
    /**
     * Submit a new weight value.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #weightLogProgressDelay(Engine)
     * @see #rxe_weightValueSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitWeightValue(@NotNull final Engine<?> ENGINE) {
        LogUtil.println("Submitting weight value");

        return rxe_weightValueSubmit(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(weightLogProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Submit a new weight entry. This action must be done after a weight
     * value has been selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #weightLogProgressDelay(Engine)
     * @see #rxe_weightEntrySubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitWeightEntry(@NotNull final Engine<?> ENGINE) {
        LogUtil.println("Submitting weight entry");

        return rxe_weightEntrySubmit(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(weightLogProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Log a new weight value, without completing the weight entry.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_swipeCSSViewRandomly(Engine)
     * @see #rxa_submitWeightValue(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeWeightValue(@NotNull final Engine<?> ENGINE) {
        final LogWeightActionType THIS = this;

        return rxa_swipeCSSViewRandomly(ENGINE)
            .flatMap(a -> THIS.rxa_submitWeightValue(ENGINE));
    }

    /**
     * Open the weight time picker.
     * On {@link org.swiften.xtestkit.mobile.Platform#IOS}, the time picker
     * is open by default, so if we click on the {@link WebElement} emitted by
     * {@link #rxe_weightTime(Engine)}, it will close. Therefore, we need to
     * check if it is visible first.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isFalse(boolean)
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightTime(Engine)
     * @see #rxv_weightTimePickerOpen(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_openWeightTimePicker(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_weightTime(ENGINE).flatMap(ENGINE::rxa_click);
        } else if (ENGINE instanceof IOSEngine) {
            final LogWeightActionType THIS = this;

            return rxv_weightTimePickerOpen(ENGINE)
                .filter(BooleanUtil::isFalse)
                .flatMap(a -> THIS.rxe_weightTime(ENGINE))
                .flatMap(ENGINE::rxa_click)
                .map(BooleanUtil::toTrue)
                .defaultIfEmpty(false);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Select weight date-time.
     * @param engine {@link Engine} instance.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_selectSpinnerDateTime(Engine, Date)
     */
    @NotNull
    default Flowable<?> rxa_selectWeightTime(@NotNull Engine<?> engine,
                                             @NotNull Date date) {
        return rxa_selectSpinnerDateTime(engine, date);
    }

    /**
     * Confirm weight time.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightTime(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmWeightTime(@NotNull final Engine<?> ENGINE) {
        return rxe_weightTime(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Complete the weight detail entry.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #weightLogProgressDelay(Engine)
     * @see #randomSelectableTime()
     * @see #rxa_openWeightTimePicker(Engine)
     * @see #rxa_selectWeightTime(Engine, Date)
     * @see #rxa_confirmWeightTime(Engine)
     * @see #rxa_submitWeightEntry(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeWeightEntry(@NotNull final Engine<?> ENGINE) {
        final LogWeightActionType THIS = this;
        final Date TIME = randomSelectableTime();

        return rxa_openWeightTimePicker(ENGINE)
            .flatMap(a -> THIS.rxa_selectWeightTime(ENGINE, TIME))
            .flatMap(a -> THIS.rxa_confirmWeightTime(ENGINE))
            .flatMap(a -> THIS.rxa_submitWeightEntry(ENGINE));
    }

    /**
     * Log a new weight by completing the weight value and weight entry.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_logWeight(@NotNull final Engine<?> ENGINE) {
        final LogWeightActionType THIS = this;

        return rxa_completeWeightValue(ENGINE)
            .flatMap(a -> THIS.rxa_completeWeightEntry(ENGINE));
    }

    /**
     * Toggle weight location.
     * @param E {@link Engine} instance.
     * @param ON {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightLocSwitch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleWeightLocation(@NotNull final Engine<?> E,
                                                 final boolean ON) {
        return rxe_weightLocSwitch(E).flatMap(a -> E.rxa_toggleSwitch(a, ON));
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT_ENTRY}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}. We do not
     * use this with {@link com.holmusk.SuperLeapQA.navigation.ScreenHolder}
     * because the flows for both platforms are different in such a way that
     * it causes {@link StackOverflowError} when we use
     * {@link org.swiften.xtestkit.navigation.ScreenManagerType#multiNodes(ScreenType...)}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_dashboardFromWeightEntry(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxa_clickBackButton(engine);
        } else if (engine instanceof IOSEngine) {
            final LogWeightActionType THIS = this;

            return Flowable.range(0, 2)
                .concatMap(a -> THIS.rxa_clickBackButton(engine))
                .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
