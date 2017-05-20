package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 19/5/17.
 */
public interface DOBPickerTestHelperType extends DOBPickerActionType {
    /**
     * Validate the DoB picker screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see Engine#rx_click(WebElement)
     * @see Engine#rx_navigateBackOnce()
     * @see #rx_e_DoBEditField(Engine)
     * @see #rx_e_DoBElements(Engine)
     * @see #generalDelay()
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_DoBPickerScreen(@NotNull final Engine<?> ENGINE) {
        long delay = generalDelay();

        return Flowable
            .mergeArray(
                rx_e_DoBEditField(ENGINE),
                ENGINE.rx_containsText("register_title_dateOfBirth"),
                ENGINE.rx_containsText(
                    "parentSignUp_title_whatIsYourChild",
                    "teenSignUp_title_whatIsYour"
                )
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> rx_e_DoBEditField(ENGINE))
            .flatMap(ENGINE::rx_click)
            .flatMap(a -> rx_e_DoBElements(ENGINE).all(ObjectUtil::nonNull).toFlowable())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rx_navigateBackOnce())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .map(BooleanUtil::toTrue);
    }

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
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @param AGES {@link List} of {@link Integer}.
     * @return {@link Flowable} instance.
     * @see UserMode#acceptableAgeCategoryRange()
     * @see #rx_a_openDoBPicker(Engine)
     * @see #rx_v_acceptableAgeScreen(Engine)
     * @see #rx_v_unacceptableAgeScreen(Engine, UserMode)
     * @see #rx_a_clickBackButton(Engine)
     */
    @NotNull
    @GuarantorAware(value = false)
    default Flowable<?> rx_h_validateDoBs(@NotNull final Engine<?> ENGINE,
                                          @NotNull final UserMode MODE,
                                          @NotNull final List<Integer> AGES) {
        final DOBPickerActionType THIS = this;
        final List<Integer> RANGE = MODE.acceptableAgeCategoryRange();
        final int LENGTH = AGES.size();

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return THIS.rx_a_openDoBPicker(ENGINE)
                        .flatMap(a -> THIS.rx_a_selectDoBToBeOfAge(ENGINE, AGE))
                        .flatMap(a -> THIS.rxConfirmDoB(ENGINE))
                        .flatMap(a -> {
                            if (VALID) {
                                return THIS.rx_v_acceptableAgeScreen(ENGINE);
                            } else {
                                return THIS.rx_v_unacceptableAgeScreen(ENGINE, MODE);
                            }
                        })
                        .flatMap(a -> THIS.rx_a_clickBackButton(ENGINE))
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new Repeater().repeat(0);
    }

    /**
     * Check that the DoB dialog has correct elements.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_openDoBPicker(Engine)
     * @see #rx_a_selectDoB(Engine, Date)
     * @see #rx_a_clickBackButton(Engine)
     * @see #rx_v_DoBEditFieldHasDate(Engine, Date)
     */
    @NotNull
    default Flowable<?> rxCheckDoBDialogHasCorrectElements(@NotNull final Engine<?> ENGINE) {
        final DOBPickerActionType THIS = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();

        return rx_a_openDoBPicker(ENGINE)
            .flatMap(a -> THIS.rx_a_selectDoB(ENGINE, DATE))
            .flatMap(a -> THIS.rxConfirmDoB(ENGINE))
            .flatMap(a -> THIS.rx_a_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rx_v_DoBEditFieldHasDate(ENGINE, DATE));
    }
}
