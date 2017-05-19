package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.number.NumberTestUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by haipham on 19/5/17.
 */
public interface DOBPickerTestHelperType extends DOBPickerActionType {
    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link UserMode#acceptableAgeCategoryRange()} should not bring the user
     * to the correct sign up screen. This action assumes the user is in the DoB
     * selection screen, but has not opened the DoB picker yet.
     * Be aware that this method is not guarantor-aware.
     * {@link UserMode#TEEN_UNDER_18} and {@link UserMode#TEEN_ABOVE_18} will
     * be treated the same. This is why we use
     * {@link UserMode#acceptableAgeCategoryRange()} instead of
     * {@link UserMode#acceptableAgeRange()}.
     * @param MODE A {@link UserMode} instance.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see UserMode#acceptableAgeCategoryRange()
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen(UserMode)
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    @GuarantorAware(value = false)
    default Flowable<Boolean> rxValidateDoBs(@NotNull final UserMode MODE,
                                             @NotNull final List<Integer> AGES) {
        final DOBPickerActionType THIS = this;
        final List<Integer> RANGE = MODE.acceptableAgeCategoryRange();
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
