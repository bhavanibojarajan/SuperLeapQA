package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.apache.bcel.generic.FLOAD;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.type.DelayType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

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
     * results in an age that does not lie within {
     * @link #acceptableAgeRange(UserMode)}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_unacceptableAgeInput(@NotNull UserMode mode) {
        int age = mode.maxAcceptableAge() + 1;
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Open the DoB dialog in the sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     * @see #rxDoBEditField()
     * @see BaseEngine#rxClick(WebElement)
     * @see BaseEngine#rxImplicitlyWait(DelayType)
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditField()
            .flatMap(ENGINE::rxClick).map(a -> true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE
                .rxElementContainingText("ok")
                .flatMap(ENGINE::rxClick)
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
     * @see BaseEngine#rxSelectDate(DateType)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoB(@NotNull final Date DATE) {
        return engine().rxSelectDate(() -> DATE);
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

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link UserMode#acceptableAgeRange()} should not bring the user to the
     * correct sign up screen. This action assumes the user is in the DoB
     * selection screen, but has not opened the DoB picker yet.
     * @param MODE A {@link UserMode} instance.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see UserMode#acceptableAgeRange()
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen(UserMode)
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final UserMode MODE,
                                             @NotNull final List<Integer> AGES) {
        final DOBPickerActionType THIS = this;
        final List<Integer> RANGE = MODE.acceptableAgeRange();
        final int LENGTH = AGES.size();

        class IterateDoBs {
            @NotNull
            @SuppressWarnings("WeakerAccess")
            Flowable<Boolean> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return THIS.rxOpenDoBPicker()
                        .flatMap(a -> THIS.rxSelectDoBToBeOfAge(AGE))
                        .flatMap(a -> THIS.rxConfirmDoB())
                        .flatMap(a -> {
                            if (VALID) {
                                return THIS.rxValidateAcceptableAgeScreen();
                            } else {
                                return THIS.rxValidateUnacceptableAgeScreen(MODE);
                            }
                        })
                        .flatMap(a -> THIS.rxNavigateBackWithBackButton())
                        .flatMap(a -> new IterateDoBs().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new IterateDoBs().repeat(0);
    }

    /**
     * Select a random DoB, assuming the user is already in the DoB selection
     * screen.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     * @see #rxConfirmDoB()
     * @see #rxNavigateBackWithBackButton()
     * @see #rxDoBEditFieldHasDate(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectRandomDoB() {
        final DOBPickerActionType THIS = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(2010, 2030));
        final Date DATE = calendar.getTime();

        // When
        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoB(DATE))
            .flatMap(a -> THIS.rxConfirmDoB())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxDoBEditFieldHasDate(DATE));
    }

    /**
     * Check that the DoB dialog has correct elements.
     * @return A {@link Flowable} instance.
     * @see #rxOpenDoBPicker()
     * @see #rxSelectDoB(Date)
     * @see #rxNavigateBackWithBackButton()
     * @see #rxDoBEditFieldHasDate(Date)
     */
    @NotNull
    default Flowable<Boolean> rxCheckDoBDialogHasCorrectElements() {
        final DOBPickerActionType THIS = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();

        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoB(DATE))
            .flatMap(a -> THIS.rxConfirmDoB())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxDoBEditFieldHasDate(DATE));
    }
}
