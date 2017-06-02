package com.holmusk.SuperLeapQA.ui.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

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
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxa_navigateBackOnce()
     * @see #rxa_openDoBPicker(Engine)
     * @see #generalDelay(Engine)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxh_DoBPickerScreen(@NotNull final Engine<?> ENGINE) {
        final DOBPickerTestHelperType THIS = this;

        return Flowable
            .mergeArray(
                ENGINE.rxe_containsText("register_title_dateOfBirth"),
                ENGINE.rxe_containsText(
                    "parentSignUp_title_whatIsYourChild",
                    "teenSignUp_title_whatIsYour"
                )
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> THIS.rxa_openDoBPicker(ENGINE))
            .delay(THIS.generalDelay(ENGINE), TimeUnit.MILLISECONDS)

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rxa_navigateBackOnce())
            .delay(THIS.generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link UserMode#validAgeCategoryRange()} should not bring the user
     * to the correct sign up screen. This action assumes the user is in the DoB
     * selection screen, but has not opened the DoB picker yet.
     * Be aware that this method is not guarantor-aware.
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same. This is why we use
     * {@link UserMode#validAgeCategoryRange()} instead of
     * {@link UserMode#validAgeRange()}.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @param AGES {@link List} of {@link Integer}.
     * @return {@link Flowable} instance.
     * @see UserMode#validAgeCategoryRange()
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxv_validAgeScreen(Engine)
     * @see #rxv_invalidAgeScreen(Engine, UserMode)
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    @GuarantorAware(value = false)
    default Flowable<?> rxh_validateDoBsRecursive(@NotNull final Engine<?> ENGINE,
                                                  @NotNull final UserMode MODE,
                                                  @NotNull final List<Integer> AGES) {
        final DOBPickerActionType THIS = this;
        final List<Integer> RANGE = MODE.validAgeCategoryRange();
        final int LENGTH = AGES.size();

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return THIS.rxa_openDoBPicker(ENGINE)
                        .flatMap(a -> THIS.rxa_selectDoBToBeOfAge(ENGINE, AGE))
                        .flatMap(a -> THIS.rxa_confirmDoB(ENGINE))
                        .flatMap(a -> {
                            if (VALID) {
                                return THIS.rxv_validAgeScreen(ENGINE);
                            } else {
                                return THIS.rxv_invalidAgeScreen(ENGINE, MODE);
                            }
                        })
                        .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new Repeater().repeat(0);
    }
}
