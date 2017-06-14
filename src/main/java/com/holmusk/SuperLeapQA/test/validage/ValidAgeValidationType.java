package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.ChoiceInput;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeValidationType extends DOBPickerValidationType {
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
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see ChoiceInput#COACH_PREF
     * @see ChoiceInput#ETHNICITY
     * @see ChoiceInput#HEIGHT
     * @see ChoiceInput#WEIGHT
     * @see Gender#MALE
     * @see Gender#FEMALE
     * @see Weight#LB
     * @see Weight#KG
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_validAgeConfirm(Engine)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_validAgeScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_editField(engine, Gender.MALE),
                rxe_editField(engine, Gender.FEMALE),
                rxe_editField(engine, ChoiceInput.HEIGHT),
                rxe_editField(engine, Height.FT),
                rxe_editField(engine, Height.CM),
                rxe_editField(engine, ChoiceInput.WEIGHT),
                rxe_editField(engine, Weight.LB),
                rxe_editField(engine, Weight.KG),
                rxe_editField(engine, ChoiceInput.ETHNICITY),
                rxe_editField(engine, ChoiceInput.COACH_PREF),
                rxe_validAgeConfirm(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if an editable field has an input.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT {@link InputType} instance.
     * @param VALUE {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#getText(WebElement)
     * @see Engine#rxv_errorWithPageSource(String)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default <P extends HMInputType> Flowable<?> rxv_hasValue(
        @NotNull final Engine<?> ENGINE,
        @NotNull final P INPUT,
        @NotNull final String VALUE
    ) {
        return rxe_editField(ENGINE, INPUT)
            .map(ENGINE::getText)
            .doOnNext(a -> LogUtil.printft("Value for %s: %s", INPUT, a))
            .filter(a -> a.toLowerCase().equals(VALUE.toLowerCase()))
            .switchIfEmpty(ENGINE.rxv_errorWithPageSource(
                String.format("Value for %s does not equal %s", INPUT, VALUE))
            )
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for numeric choice inputs (e.g. {@link Height}
     * and {@link Weight}).
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_numericChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("btnDone")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("input_title_done")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the confirm button for text choice inputs (e.g.
     * {@link ChoiceInput#ETHNICITY} or {@link ChoiceInput#COACH_PREF}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_textChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("input_title_done")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the view that pops up when an error is notified to the user. This
     * only works in specific cases however, so use with care.
     * @param error {@link LCFormat} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_errorPopup(@NotNull Engine<?> engine,
                                                @NotNull LCFormat error) {
        return engine.rxe_containsText(error).firstElement().toFlowable();
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error {@link LCFormat} value.
     * @return {@link Flowable} instance.
     * @see #rxe_errorPopup(Engine, LCFormat)
     */
    @NotNull
    default Flowable<?> rxv_isShowingError(@NotNull Engine<?> engine,
                                           @NotNull LCFormat error) {
        return rxe_errorPopup(engine, error);
    }

    /**
     * Verify the unqualified BMI pop-up.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
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
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if there is any dialog blocking the screen. This is because
     * when we scroll to bottom to review the submit button, the touch action
     * may accidentally open up a picker dialog.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isFalse(boolean)
     * @see Engine#rxe_containsID(String...)
     */
    @NotNull
    default Flowable<Boolean> rxv_dialogBlockingScreen(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("select_dialog_listview", "btnDone")
                .isEmpty()
                .map(BooleanUtil::isFalse)
                .onErrorReturnItem(false)
                .toFlowable();
        } else {
            return Flowable.just(false);
        }
    }
}
