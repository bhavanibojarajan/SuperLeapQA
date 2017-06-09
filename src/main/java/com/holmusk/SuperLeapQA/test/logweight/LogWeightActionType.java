package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollActionType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

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
     * @see #rxe_weightSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitWeightValue(@NotNull final Engine<?> ENGINE) {
        return rxe_weightSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Submit a new weight entry. This action must be done after a weight
     * value has been selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightEntrySubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitWeightEntry(@NotNull final Engine<?> ENGINE) {
        return rxe_weightEntrySubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Log a new weight value, without completing the weight entry.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #weightLogProgressDelay(Engine)
     * @see #rxa_swipeCSSViewRandomly(Engine)
     * @see #rxa_submitWeightValue(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeWeightValue(@NotNull final Engine<?> ENGINE) {
        final LogWeightActionType THIS = this;

        return rxa_swipeCSSViewRandomly(ENGINE)
            .flatMap(a -> THIS.rxa_submitWeightValue(ENGINE))
            .delay(weightLogProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Open the weight time picker.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightTime(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openWeightTimePicker(@NotNull final Engine<?> ENGINE) {
        return rxe_weightTime(ENGINE).flatMap(ENGINE::rxa_click);
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
            .flatMap(a -> THIS.rxa_submitWeightEntry(ENGINE))
            .delay(weightLogProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
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
}
