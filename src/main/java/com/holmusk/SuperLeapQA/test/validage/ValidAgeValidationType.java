package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.ChoiceInput;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerValidationType;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeValidationType extends BaseValidationType, DOBPickerValidationType {
    /**
     * Get the next confirm button for acceptable age input screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_validAgeConfirm(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_next")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_validAgeConfirm(Engine)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_validAgeScreen(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .mergeArray(
                rxe_editField(ENGINE, Gender.MALE),
                rxe_editField(ENGINE, Gender.FEMALE),
                rxe_editField(ENGINE, ChoiceInput.HEIGHT),
                rxe_editField(ENGINE, ChoiceInput.WEIGHT),
                rxe_editField(ENGINE, ChoiceInput.ETHNICITY),
                rxe_editField(ENGINE, ChoiceInput.COACH_PREF),
                rxe_validAgeConfirm(ENGINE),

                Flowable.create(obs -> {
                    if (ENGINE instanceof AndroidEngine) {
                        /* The following elements only appear when we are
                         * testing on Android */
                        obs.onNext(true);
                    }

                    obs.onComplete();
                }, BackpressureStrategy.BUFFER
                ).flatMap(a -> Flowable.mergeArray(
                    rxe_editField(ENGINE, Height.FT),
                    rxe_editField(ENGINE, Height.CM),
                    rxe_editField(ENGINE, Weight.LB),
                    rxe_editField(ENGINE, Weight.KG)
                ))
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Verify the unqualified BMI pop-up.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_unqualifiedBMI(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("register_title_bmiUnqualified"),
                engine.rxe_containsText(
                    "register_title_ok",
                    "register_title_close"
                )
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Check if there is any dialog blocking the screen. This is because
     * when we scroll to bottom to review the submit button, the touch action
     * may accidentally open up a picker dialog.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     */
    @NotNull
    default Flowable<Boolean> rxv_dialogBlockingScreen(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("select_dialog_listview", "btnDone")
                .isEmpty()
                .map(HPBooleans::isFalse)
                .onErrorReturnItem(false)
                .toFlowable();
        } else {
            return Flowable.just(false);
        }
    }
}
