package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.type.DelayType;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerActionType extends DOBPickerValidationType, SignUpActionType {
    //region Bridged Navigation
    /**
     * Navigate to the appropriate screen, based on an age value.
     * @param AGE An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxOpenDoBPicker()
     * @see #rxSelectDoBToBeOfAge(int)
     * @see #rxConfirmDoB()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_inputScreenForAge(final int AGE) {
        final DOBPickerActionType THIS = this;

        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoBToBeOfAge(AGE))
            .flatMap(a -> THIS.rxConfirmDoB());
    }

    /**
     * Bridge method that helps navigate from splash screen to acceptable
     * age input.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_acceptableAge(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_acceptableAge(@NotNull final UserMode MODE) {
        final DOBPickerActionType THIS = this;
        return rx_splash_DoBPicker(MODE).flatMap(a -> THIS.rx_DoBPicker_acceptableAge(MODE));
    }

    /**
     * Bridge method that helps navigate from splash screen to unacceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_unacceptableAgeInput(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_unacceptableAgeInput(@NotNull UserMode mode) {
        final DOBPickerActionType THIS = this;
        return rx_splash_DoBPicker(mode).flatMap(a -> THIS.rx_DoBPicker_unacceptableAgeInput(mode));
    }
    //endregion

    //region Navigation
    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link UserMode#acceptableAgeRange()}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAge(@NotNull UserMode mode) {
        List<Integer> range = mode.acceptableAgeRange();
        int age = CollectionTestUtil.randomElement(range);
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within
     * {@link UserMode#maxCategoryAcceptableAge()})
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see UserMode#maxCategoryAcceptableAge()
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    @GuarantorAware(value = false)
    default Flowable<Boolean> rx_DoBPicker_unacceptableAgeInput(@NotNull UserMode mode) {
        int age = mode.maxCategoryAcceptableAge() + 1;
        return rx_DoBPicker_inputScreenForAge(age);
    }
    //endregion

    /**
     * Open the DoB dialog in the sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     * @see #rxDoBEditField()
     * @see BaseEngine#rx_click(WebElement)
     * @see BaseEngine#rx_implicitlyWait(DelayType)
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditField()
            .flatMap(ENGINE::rx_click).map(a -> true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rx_implicitlyWait(this::generalDelay))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see BaseEngine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();

        if (ENGINE instanceof AndroidEngine) {
            return ENGINE
                .rx_elementContainingText("ok")
                .flatMap(ENGINE::rx_click)
                .map(BooleanUtil::toTrue);
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param DATE A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_selectDate(DateType)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoB(@NotNull final Date DATE) {
        return engine().rx_selectDate(() -> DATE);
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param age An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoBToBeOfAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return rxSelectDoB(calendar.getTime());
    }
}
