package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Pair;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.mobile.Platform;

import java.util.List;

/**
 * Created by haipham on 19/5/17.
 */
public interface AcceptableAgeTestHelperType extends AcceptableAgeActionType {
    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(SLInputType)
     * @see #rxSelectNumericInput(List)
     * @see #rxEditFieldHasValue(SLInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateAcceptableAgeInputs(@NotNull UserMode mode) {
        final AcceptableAgeActionType THIS = this;
        final List<Pair<Height,Double>> HEIGHT_M = Height.randomMetric(mode);
        final List<Pair<Weight,Double>> WEIGHT_M = Weight.randomMetric(mode);
        final List<Pair<Height,Double>> HEIGHT_I = Height.randomImperial(mode);
        final List<Pair<Weight,Double>> WEIGHT_I = Weight.randomImperial(mode);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return rxClickInputField(Gender.MALE)
            .flatMap(a -> THIS.rxClickInputField(Gender.FEMALE))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))
            .flatMap(a -> THIS.rxSelectCoachPref(CP))

            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_CM))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_M))
            .flatMap(a -> THIS.rxConfirmNumericChoiceInput())

            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_FT))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_I))
            .flatMap(a -> THIS.rxConfirmNumericChoiceInput())

            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_KG))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_M))
            .flatMap(a -> THIS.rxConfirmNumericChoiceInput())

            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_LB))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_I))
            .flatMap(a -> THIS.rxConfirmNumericChoiceInput());
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxConfirmAcceptableAgeInputs()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateAcceptableAgeEmptyInputErrors(@NotNull final UserMode MODE) {
        final AcceptableAgeActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (ENGINE.platform().equals(Platform.IOS)) {
            return Flowable.just(true);
        }

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final double HEIGHT_CM = Height.CHILD_CM.randomValue();
        final double HEIGHT_FT = Height.CHILD_FT.randomValue();
        final double WEIGHT_KG = Weight.CHILD_KG.randomValue();
        final double WEIGHT_LB = Weight.CHILD_LB.randomValue();
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rxConfirmAcceptableAgeInputs()
            .flatMap(a -> THIS.rxIsShowingError(GENDER.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxClickInputField(GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_CM))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CHILD_CM.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CHILD_CM, HEIGHT_CM))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_FT))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CHILD_FT.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CHILD_FT, HEIGHT_FT))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_KG))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.CHILD_KG.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.CHILD_KG, WEIGHT_KG))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_LB))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.CHILD_LB.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.CHILD_LB, WEIGHT_LB))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.ETHNICITY.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.COACH_PREFERENCE.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectCoachPref(CP));
    }
}
