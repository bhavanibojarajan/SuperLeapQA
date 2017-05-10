package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.CalendarElement;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface CommonRegisterInteractionType extends
    BaseInteractionType,
    CommonRegisterValidationType,
    PlatformErrorType
{
    /**
     * Open the DoB dialog in the parent sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditableField()
            .flatMap(ENGINE::rxClick)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay));
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE.rxElementContainingText("ok").flatMap(ENGINE::rxClick);
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param DATE A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxSelectDate(DateType)
     * @see #rxConfirmDoB()
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
        calendar.set(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.YEAR, -age);
        return rxSelectDoB(calendar.getTime());
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link #acceptableAgeRange()} should not bring the user to the correct
     * sign up screen. This action assumes the user is in the DoB selection
     * screen, but has not opened the DoB picker yet.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see #acceptableAgeRange()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final List<Integer> AGES) {
        final List<Integer> ACCEPTABLE = acceptableAgeRange();
        final int LENGTH = AGES.size();

        class IterateDoBs {
            @NotNull
            @SuppressWarnings("WeakerAccess")
            Flowable<Boolean> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = ACCEPTABLE.contains(AGE);

                    return rxOpenDoBPicker()
                        .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
                        .flatMap(a -> rxConfirmDoB())
                        .flatMap(a -> {
                            if (VALID) {
                                return Flowable.just(true);
                            } else {
                                return rxValidateUnacceptableAgeScreen();
                            }
                        })
                        .flatMap(a -> rxNavigateBackWithBackButton())
                        .flatMap(a -> new IterateDoBs().repeat(INDEX + 1));
                } else {
                    return Flowable.empty();
                }
            }
        }

        return new IterateDoBs().repeat(0);
    }
}
