package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableInputValidationType extends DOBPickerValidationType {
    /**
     * Get the next confirm button for acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxAcceptableAgeConfirmButton() {
        return engine().rx_elementContainingText("register_title_next");
    }

    /**
     * Get the back button's title label.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxAcceptableAgeInputTitleLabel() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rx_elementContainingText(
                "parentSignUp_title_enterChildDetails",
                "teenSignUp_title_enterDetails"
            );
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the scrollable height selector view, assuming the user is already
     * in the height picker window.
     * @param input A {@link ChoiceInput} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementWithXPath(XPath...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default <P extends AndroidChoiceInputType> Flowable<WebElement>
    rxScrollableChoicePicker(@NotNull P input) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rx_elementWithXPath(input.androidScrollViewPickerXPath());
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get all input value items within the scrollable view as emitted by
     * {@link #rxScrollableChoicePicker(AndroidChoiceInputType)}, assuming
     * the user is already in the picker window.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementsByXPath(ByXPath)
     * @see BaseEngine#newXPathBuilder()
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<WebElement> rxPickerItemViews(@NotNull P input) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rx_elementWithXPath(input.androidScrollViewItemXPath());
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @return A {@link Flowable} instance.
     * @see #rx_editFieldForInput(SLInputType)
     * @see #rxAcceptableAgeConfirmButton()
     * @see #rxAcceptableAgeInputTitleLabel()
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rx_validateAcceptableAgeScreen() {
        return Flowable
            .concatArray(
                rx_editFieldForInput(Gender.MALE),
                rx_editFieldForInput(Gender.FEMALE),
                rx_editFieldForInput(ChoiceInput.HEIGHT),
                rx_editFieldForInput(Height.FT),
                rx_editFieldForInput(Height.CM),
                rx_editFieldForInput(ChoiceInput.WEIGHT),
                rx_editFieldForInput(Weight.LB),
                rx_editFieldForInput(Weight.KG),
                rx_editFieldForInput(ChoiceInput.ETHNICITY),
                rx_editFieldForInput(ChoiceInput.COACH_PREF),
                rxAcceptableAgeConfirmButton(),
                rxAcceptableAgeInputTitleLabel()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if an editable field has an input.
     * @param INPUT A {@link InputType} instance.
     * @param VALUE A {@link String} value.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see #rx_editFieldForInput(SLInputType)
     */
    @NotNull
    default <P extends SLInputType>
    Flowable<Boolean> rx_editFieldHasValue(@NotNull final P INPUT,
                                           @NotNull final String VALUE) {
        return rx_editFieldForInput(INPUT)
            .map(engine()::getText)
            .doOnNext(a -> LogUtil.printfThread("Current value for %s: %s", INPUT, a))
            .filter(a -> a.equals(VALUE))
            .switchIfEmpty(RxUtil.errorF("Value for %s does not equal %s", INPUT, VALUE))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for numeric choice inputs (e.g. {@link Height}
     * and {@link Weight}).
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingID(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_numericChoiceInputConfirmButton() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rx_elementContainingID("btnDone");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the view that pops up when an error is notified to the user. This
     * only works in specific cases however, so use with care.
     * @param error A {@link LCFormat} value.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxErrorPopup(@NotNull LCFormat error) {
        return engine().rx_elementContainingText(error);
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error A {@link LCFormat} value.
     * @return A {@link Flowable} instance.
     * @see #rxErrorPopup(LCFormat)
     */
    @NotNull
    default Flowable<Boolean> rxIsShowingError(@NotNull LCFormat error) {
        return rxErrorPopup(error).map(a -> true);
    }
}
