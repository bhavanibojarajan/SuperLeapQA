package com.holmusk.SuperLeapQA.ui.signup.validage;

import com.holmusk.SuperLeapQA.model.ChoiceInput;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeValidationType extends DOBPickerValidationType {
    /**
     * Get the next confirm button for acceptable age input screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_validAgeConfirm(@NotNull Engine<?> engine) {
        return engine.rx_containsText("register_title_next").firstElement().toFlowable();
    }

    /**
     * Get the back button's title label.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_validAgeInputTitle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsText(
                "parentSignUp_title_enterChildDetails",
                "teenSignUp_title_enterDetails"
            ).firstElement().toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see #rx_e_validAgeConfirm(Engine)
     * @see #rx_e_validAgeInputTitle(Engine)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_validAgeScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rx_e_editField(engine, Gender.MALE),
                rx_e_editField(engine, Gender.FEMALE),
                rx_e_editField(engine, ChoiceInput.HEIGHT),
                rx_e_editField(engine, Height.FT),
                rx_e_editField(engine, Height.CM),
                rx_e_editField(engine, ChoiceInput.WEIGHT),
                rx_e_editField(engine, Weight.LB),
                rx_e_editField(engine, Weight.KG),
                rx_e_editField(engine, ChoiceInput.ETHNICITY),
                rx_e_editField(engine, ChoiceInput.COACH_PREF),
                rx_e_validAgeConfirm(engine),
                rx_e_validAgeInputTitle(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if an editable field has an input.
     * @param INPUT {@link InputType} instance.
     * @param VALUE {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see Engine#getText(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default <P extends SLInputType> Flowable<?>
    rx_v_editFieldHasValue(@NotNull final Engine<?> ENGINE,
                           @NotNull final P INPUT,
                           @NotNull final String VALUE) {
        return rx_e_editField(ENGINE, INPUT)
            .map(ENGINE::getText)
            .doOnNext(a -> LogUtil.printfThread("Current value for %s: %s", INPUT, a))
            .filter(a -> a.equals(VALUE))
            .switchIfEmpty(RxUtil.errorF("Value for %s does not equal %s", INPUT, VALUE))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for numeric choice inputs (e.g. {@link Height}
     * and {@link Weight}).
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_numericChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_containsID("btnDone")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rx_containsText("input_title_done")
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
     * @see Engine#rx_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_textChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rx_containsText("input_title_done")
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
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_errorPopup(@NotNull Engine<?> engine,
                                                 @NotNull LCFormat error) {
        return engine.rx_containsText(error).firstElement().toFlowable();
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error {@link LCFormat} value.
     * @return {@link Flowable} instance.
     * @see #rx_e_errorPopup(Engine, LCFormat)
     */
    @NotNull
    default Flowable<?> rx_isShowingError(@NotNull Engine<?> engine,
                                          @NotNull LCFormat error) {
        return rx_e_errorPopup(engine, error);
    }
}
